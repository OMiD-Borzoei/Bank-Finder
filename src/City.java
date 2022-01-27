import java.util.Scanner;

public class City {

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    static KDTree cityAllBanks;
    static NeighborhoodDB cityNeighborhoods;
    static MainBanksDB cityMainBanks;
    int mostBranches;
    Bank mostBrBank;
    Control controller;
    static Scanner sc = new Scanner(System.in);

    City() {
        cityAllBanks = new KDTree();
        cityNeighborhoods = new NeighborhoodDB();
        cityMainBanks = new MainBanksDB();
        this.controller = new Control();
        this.mostBranches = 0;
        this.mostBrBank = null;
    }

    String getA_zString() {
        String name;
        while (true) {
            int i;
            name = sc.nextLine();
            for (i = 0; i < name.length(); i++)
                if ((name.charAt(i) < 'a' || name.charAt(i) > 'z') && name.charAt(i) != ' ') {
                    System.err.println("use Only a-z characters including space, Try again :");
                    break;
                }
            if (i == name.length())
                break;
        }
        return name.trim();
    }

    void addNeighborhood() {
        System.out.print("Enter Neighborhood's Name : ");
        String name = getA_zString();
        double lE, dE, rE, uE;
        try {
            System.out.print("Enter Rectangle's Coordinates (bottomLeft X & Y) : ");
            lE = sc.nextDouble();
            dE = Double.parseDouble(sc.nextLine());
            System.out.print("Enter Rectangle's Coordinates (topRight X & Y) : ");
            rE = sc.nextDouble();
            uE = Double.parseDouble(sc.nextLine());
            if (rE - lE <= 0 || uE - dE <= 0)
                throw new ArithmeticException();
            if (cityNeighborhoods.add(new Neighborhood(name, lE, dE, rE, uE))) {
                controller.Add("addN", name);
                System.out.println(ANSI_BLUE + "Neighborhood \"" + name + "\" successfully added to city" + ANSI_RESET);
            } else
                System.err.println("There's already a Neighborhood named \"" + name + "\" in the city");
        } catch (NumberFormatException e) {
            System.err.println("Invalid Coordinates ! addition FAILED !!");
        } catch (ArithmeticException e) {
            System.err.println("There Can't Be A rectangle with the coordinates you Entered !");
        }
    }

    void addBank() {
        Coordinate c;
        System.out.print("Enter Bank's Name : ");
        String name = getA_zString();
        try {
            System.out.print("Enter Coordinates (x,y) : ");
            c = new Coordinate(sc.nextDouble(), Double.parseDouble(sc.nextLine()));
            if (cityAllBanks.search(c) != null) {
                System.err.println("These Coordinates Belong to another Bank :P");
                return;
            }
            mainBank mb = new mainBank(c, name);
            if (cityMainBanks.add(mb)) {
                cityAllBanks.add(mb);
                controller.Add("addB", name);
                System.out.println(ANSI_BLUE + "mainBank \"" + name + "\" successfully added to city" + ANSI_RESET);
            } else
                System.err.println("There's already a mainBank named \"" + name + "\" in the city");
        } catch (Exception e) {
            System.err.println("Invalid Coordinates ! addition FAILED !!");
        }
    }

    void addBranch() {
        Coordinate c;
        System.out.print("Enter Main Bank's Name : ");
        String bankName = getA_zString();
        String name;

        if (cityMainBanks.search(bankName) != null) {
            try {
                System.out.print("Enter Coordinates (x,y) : ");
                c = new Coordinate(sc.nextDouble(), Double.parseDouble(sc.nextLine()));
                System.out.print("Enter Branch's Name : ");
                name = getA_zString();

                if (cityAllBanks.search(c) != null) {
                    System.err.println("These Coordinates belong to another Bank !");
                } else {
                    Branch br = new Branch(c, bankName, name);
                    cityMainBanks.alter(bankName, br, "add"); //adding this branch to its mainBank
                    cityAllBanks.add(br); //adding it to the whole KDTree
                    controller.Add("addBr", br);
                    mainBank mainB = cityMainBanks.search(br.bankName);
                    if (mainB.branches.size > this.mostBranches) {
                        this.mostBranches = mainB.branches.size;
                        this.mostBrBank = mainB;
                    }
                    System.out.println(ANSI_BLUE + "Branch \"" + name + "\" successfully added to Bank \"" + bankName + "\" !" + ANSI_RESET);
                }
            } catch (Exception e) {
                System.err.println("Invalid Coordinates ! addition FAILED !!");
            }
        } else
            System.err.println("There's No Main Bank with the name you entered in this City !");
    }

    void deleteBranch() {
        Coordinate c;
        try {
            System.out.print("Enter desired Coordinate (x,y) : ");
            c = new Coordinate(sc.nextDouble(), Double.parseDouble(sc.nextLine()));
            Bank b = cityAllBanks.search(c);
            if (b == null)
                System.err.println("No Bank Found In those Coordinates");
            else if (b instanceof mainBank)
                System.err.println("Those Coordinates Belong to a mainBank, You Can't delete a mainBank");
            else if (b instanceof Branch br) {
                cityMainBanks.alter(br.bankName, br, "delete");
                cityAllBanks.delete(br.coordinate);
                controller.Add("delBr", br);
                if (this.mostBrBank.name.equals(br.bankName)) {
                    mainBank mb = (mainBank) cityMainBanks.searchEqBr(this.mostBranches);
                    if (mb != null)
                        this.mostBrBank = mb;
                    else
                        this.mostBranches--;
                }
                System.out.println(ANSI_BLUE + "Entered Coordinates belonged to branch \"" + b.name +
                        "\" from mainBank \"" + ((Branch) b).bankName + "\" " +
                        " which was SUCCESSFULLY deleted" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.err.println("Invalid Coordinates ! deletion FAILED !!");
        }

    }

    void nearestBank() {
        Coordinate c;
        try {
            System.out.print("Enter Coordinates (x,y): ");
            c = new Coordinate(sc.nextDouble(), Double.parseDouble(sc.nextLine()));
            Bank nearestBank = cityAllBanks.findNearest(c);
            if (nearestBank == null)
                System.err.println("There Are No Banks In The City !");
            else if (nearestBank instanceof Branch br) {
                System.out.println(ANSI_BLUE + "Distance : " + KDTree.bestDist);
                System.out.println("Nearest Bank Found Is \"" + br.name + "\" which is a Branch Of \"" + br.bankName + "\" ");
                System.out.println("Coordinates==>\tx: " + br.coordinate.x + ", y: " + br.coordinate.y + ANSI_RESET);
            } else {
                mainBank mb = (mainBank) nearestBank;
                System.out.println(ANSI_BLUE + "Distance : " + KDTree.bestDist);
                System.out.println("Nearest Bank Found Is \"" + mb.name + "\" which is a mainBank");
                System.out.println("Coordinates ==>    x: " + mb.coordinate.x + ", y: " + mb.coordinate.y + ANSI_RESET);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid Coordinates !");
        }
    }

    void nearestBranch() {
        Coordinate c;
        System.out.print("Enter bankName : ");
        String bankName = getA_zString();
        mainBank mb = cityMainBanks.search(bankName);
        try {
            if (mb == null)
                throw new NullPointerException();
            System.out.print("Enter Coordinates (x,y) : ");
            c = new Coordinate(sc.nextDouble(), Double.parseDouble(sc.nextLine()));
            Branch br = (Branch) mb.branches.findNearest(c);
            if (br == null)
                System.err.println("The mainBank \"" + mb.name + "\" doesn't have any Branches !");
            else {
                System.out.println(ANSI_BLUE + "Distance : " + KDTree.bestDist);
                System.out.println("Nearest Branch of \"" + bankName + "\" is \"" + br.name + "\"");
                System.out.println("Coordinates==>\tx: " + br.coordinate.x + ", y: " + br.coordinate.y + ANSI_RESET);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid Coordinates !");
        } catch (NullPointerException e) {
            System.err.println("The City doesn't have any mainBank with that name !");
        }

    }

    void printAllBranches() {
        System.out.print("Enter mainBank's Name : ");
        String bankName = getA_zString();
        try {
            cityMainBanks.search(bankName).branches.print();
            //System.out.println(ANSI_YELLOW + "Omid Has No Branches" + ANSI_RESET);
        } catch (Exception e) {
            System.err.println("MainBank Not Found !");
        }
    }

    void printAllInNeighborhood() {
        System.out.print("Enter name of the Neighborhood : ");
        String name = getA_zString();
        Neighborhood n = cityNeighborhoods.search(name);
        if (n == null)
            System.err.println("Neighborhood not Found !");
        else {
            cityAllBanks.allInNeighbor(n);
        }
    }

    void printAllInRadius() {
        Coordinate c;
        double r;
        try {
            System.out.print("Enter Coordinates : ");
            c = new Coordinate(sc.nextDouble(), Double.parseDouble(sc.nextLine()));
        } catch (Exception e) {
            System.err.println("Invalid Coordinates ! ");
            return;
        }
        try {
            System.out.print("Enter radius : ");
            r = Double.parseDouble(sc.nextLine());
            cityAllBanks.allInRadius(c, r);
        } catch (Exception e) {
            System.err.println("Invalid Radius !");
        }
    }

    void undo() {
        int x;
        try {
            System.out.print("Enter the Time you want to Travel to : ");
            x = Integer.parseInt(sc.nextLine());
            if (x < 0)
                throw new ArithmeticException();
            int result = controller.undo(x);
            switch (result) {
                case 2 -> System.out.println(ANSI_BLUE + "Undo Successful, You Are At time " + x + " Now !" + ANSI_RESET);
                case 1 -> System.out.println(ANSI_YELLOW + "We Are Already At Time " + x + ANSI_RESET);
                case 0 -> System.out.println(ANSI_YELLOW + "We haven't reached Time " + x + " Yet !" + ANSI_RESET);
            }

        } catch (Exception e) {
            System.err.println("You Must Enter A non-Negative Integer");
        }
    }

    void evaluate(String command) {
        switch (command) {
            case "addN" -> addNeighborhood();
            case "addB" -> addBank();
            case "addBr" -> addBranch();
            case "delBr" -> deleteBranch();
            case "listB" -> printAllInNeighborhood();
            case "listBrs" -> printAllBranches();
            case "nearB" -> nearestBank();
            case "nearBr" -> nearestBranch();
            case "availB" -> printAllInRadius();

            //Preferential Commands :
            case "mostBrs" -> {
                if (this.mostBrBank != null)
                    System.out.println(ANSI_BLUE + "Bank \"" + this.mostBrBank.name + "\" With " + this.mostBranches + " Branch(es)" + ANSI_RESET);
                else
                    System.out.println(ANSI_YELLOW + "There's No Bank With even One Branch in this City Yet !" + ANSI_RESET);
            }
            case "undo" -> undo();

            //My Commands :
            case "listAll" -> cityAllBanks.print();
            case "listMain" -> cityMainBanks.listAll();
            case "listN" -> cityNeighborhoods.listAll();
            case "giveTime" -> controller.st.ll.print();

            default -> System.out.println(ANSI_YELLOW + "Undefined Command" + ANSI_RESET);
        }
    }
}


class start {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        City mc = new City();
        String command;

        while (true) {
            command = sc.nextLine();
            if (command.equals("exit"))
                break;
            mc.evaluate(command.trim());
        }
        sc.close();
    }
}