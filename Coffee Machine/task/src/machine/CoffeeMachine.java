package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        do {
            if (Machine.activeState == Machine.State.CHOOSING_ACTION) {
                System.out.println("Write action (buy, fill, take, remaining, exit):");
            } else if (Machine.activeState == Machine.State.CHOOSING_RECIPE) {
                System.out.println("What do you want to buy? 1 - espresso, " +
                        "2 - latte, 3 - cappuccino, back - to main menu: ");
            }
            Machine.process(scanner.nextLine());
            System.out.println();
        } while (Machine.activeState != Machine.State.DISABLED);

    }
}

class Machine {
    private static int water = 400;
    private static int milk = 540;
    private static int coffee = 120;
    private static int cups = 9;
    private static int money = 550;

    public enum Recipe {
        ESPRESSO (250, 0, 16, 4),
        LATTE (350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6);

        int water;
        int milk;
        int coffee;
        int cost;

        Recipe(int water, int milk, int coffee, int cost) {
            this.water = water;
            this.milk = milk;
            this.coffee = coffee;
            this.cost = cost;
        }
    }

    public enum State {
        DISABLED,
        CHOOSING_ACTION,
        CHOOSING_RECIPE
    }

    public static State activeState = State.CHOOSING_ACTION;

    final private static Scanner scanner = new Scanner(System.in);

    public static void setWater(int water) {
        if (water < 0) water = 0;
        Machine.water = water;
    }

    public static void setMilk(int milk) {
        if (milk < 0) milk = 0;
        Machine.milk = milk;
    }

    public static void setCoffee(int coffee) {
        if (coffee < 0) coffee = 0;
        Machine.coffee = coffee;
    }

    public static void setCups(int cups) {
        if (cups < 0) cups = 0;
        Machine.cups = cups;
    }

    public static void setMoney(int money) {
        Machine.money = money;
    }

    public static int getWater() {
        return Machine.water;
    }

    public static int getMilk() {
        return Machine.milk;
    }

    public static int getCoffee() {
        return Machine.coffee;
    }

    public static int getCups() {
        return Machine.cups;
    }

    public static int getMoney() {
        return Machine.money;
    }

    public static void process(String s) {

        switch (activeState) {
            case DISABLED:
                break;
            case CHOOSING_ACTION:
                switch (s) {
                    case "remaining":
                        printState();
                        break;
                    case "exit":
                        activeState = State.DISABLED;
                        break;
                    case "buy":
                        activeState = State.CHOOSING_RECIPE;
                        break;
                    case "fill":
                        Machine.fill();
                        break;
                    case "take":
                        Machine.take();
                        break;
                }
                break;
            case CHOOSING_RECIPE:
                switch (s) {
                    case "1":
                        doCoffee(Recipe.ESPRESSO);
                        break;
                    case "2":
                        doCoffee(Recipe.LATTE);
                        break;
                    case "3":
                        doCoffee(Recipe.CAPPUCCINO);
                        break;
                    case "back":
                        activeState = State.CHOOSING_ACTION;
                        break;
                }
                activeState = State.CHOOSING_ACTION;
                break;
        }
    }

    public static void doCoffee(Recipe recipe) {
        String missingIngridient = "none";

        if (getWater() < recipe.water) { missingIngridient = "water"; }
        else if (getMilk() < recipe.milk && "none".equals(missingIngridient)) {
            missingIngridient = "milk";
        }
        else if (getCoffee() < recipe.coffee && "none".equals(missingIngridient)) {
            missingIngridient = "coffee";
        }
        else if (getCups() == 0 && "none".equals(missingIngridient)) {
            missingIngridient ="cups";
        }

        if ("none".equals(missingIngridient)) {
            setWater(getWater() - recipe.water);
            setMilk(getMilk() - recipe.milk);
            setCoffee(getCoffee() - recipe.coffee);
            setMoney(getMoney() + recipe.cost);
            setCups(getCups() - 1);
            System.out.println("I have enough resources, making you a coffee!");
        } else {
            System.out.printf("Sorry, not enough %s!\n", missingIngridient);
        }
    }

    public static void fill() {
        System.out.println("Write how many ml of water do you want to add:");
        setWater(getWater() + scanner.nextInt());
        System.out.println("Write how many ml of milk do you want to add:");
        setMilk(getMilk() + scanner.nextInt());
        System.out.println("Write how many grams of coffee beans do you want to add:");
        setCoffee(getCoffee() + scanner.nextInt());
        System.out.println("Write how many disposable cups of coffee do you want to add: ");
        setCups(getCups() + scanner.nextInt());
    }

    public static void take() {
        System.out.printf("I gave you $%d\n", getMoney());
        setMoney(0);
    }

    public static void printState() {
        System.out.println("The coffee machine has:");
        System.out.printf("%d of water\n", getWater());
        System.out.printf("%d of milk\n", getMilk());
        System.out.printf("%d of coffee beans\n", getCoffee());
        System.out.printf("%d of disposable cups\n", getCups());
        System.out.printf("%d of money\n", getMoney());
    }
}

