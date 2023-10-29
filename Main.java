//imports
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // initialise variables
        // population
        int citizens = 95;
        int starved = 0;
        int arrivals = 5;
        int bushels_feed = 0;
        double proportion_starved = 0;
        int deaths = 0;
        // needed for calculating the final proportion of starved
        int deaths_by_starving = 0;

        // farming
        int farm_land = 1000;
        int bushels_per_acre = 3;
        int store = 2800;
        int rat_food = 200;
        int planted = 0;

        // player input
        boolean valid_input = false;
        int player_input = 0;
        Scanner input_scanner = new Scanner(System.in);

        // other
        int plague_chance = 0;
        int price_of_land = 0;
        Random random_generator = new Random();



        // main loop - years_ruled is the index
        for (int years_ruled = 0; years_ruled < 10; years_ruled++) {
            // Population Report
            System.out.println("\nHamurabi: I beg to report to you, in year " + years_ruled + ',');
            System.out.println(starved + " people starved, and " + arrivals + " came to the city.");

            citizens += arrivals;
            citizens -= starved;

            // prevent from being negative
            if (citizens < 0) {
                citizens = 0;
            }



            // Plague Check
            if (random_generator.nextInt(1,100) <= plague_chance) {
                System.out.println("A Horrible plague struck - half the people died!");
                int old_citizens = citizens;
                citizens /= 2;
                deaths += old_citizens - citizens;
            }

            // Final population
            System.out.println("The population is now "+ citizens + ".");

            // early exit by everyone dying
            if (citizens == 0) {
                System.out.println("Everyone has died!!");
                national_Fink();
            }



            // Harvest Report
            System.out.println("\nThe city now owns "+farm_land+" acres of farmland.");
            System.out.println("You harvested " + bushels_per_acre + " bushels per acre.");
            System.out.println("Rats ate "+rat_food+" bushels.");
            System.out.println("You now have "+store+" bushels in store.");



            // Trade Land
            price_of_land = random_generator.nextInt(17,27);
            System.out.println("\nLand is trading at "+price_of_land+" bushels per acre.");
            System.out.println("How many acres do you wish to buy?");

            valid_input = false;
            while (!valid_input) {
                player_input = input_scanner.nextInt();
                // negative number check
                if (player_input < 0) {
                    System.out.println("\nHamurabi: I cannot do what you wish.");
                    System.out.println("Get yourself another steward!");
                    System.exit(0);
                }
                // can afford check
                if (player_input * price_of_land > store) {
                    System.out.println("\nHamurabi: Think again. You only have "+store+" bushels of grain, now then.");
                    System.out.println("How many acres do you wish to buy?");
                } else {
                    // transaction
                    farm_land += player_input;
                    store -= (player_input * price_of_land);
                    valid_input = true;
                }
            }



            // feed the people
            valid_input = false;
            while (!valid_input) {
                System.out.println("How many bushels do you wish to feed to your people?");
                player_input = input_scanner.nextInt();
                // negative number check
                if (player_input < 0) {
                    System.out.println("\nHamurabi: I cannot do what you wish.");
                    System.out.println("Get yourself another steward!");
                    System.exit(0);
                }
                // can afford check
                if (player_input > store) {
                    System.out.println("\nHamurabi: Think again. You only have "+store+" bushels of grain, now then.");
                } else {
                    // process input
                    store -= player_input;
                    bushels_feed = player_input;
                    valid_input = true;
                }
            }



            // Plant Crops
            valid_input = false;
            while (!valid_input) {
                System.out.println("\nHow many acres do you wish to plant with seed?");
                player_input = input_scanner.nextInt();
                // negative number check
                if (player_input < 0) {
                    System.out.println("\nHamurabi: I cannot do what you wish.");
                    System.out.println("Get yourself another steward!");
                    System.exit(0);
                }

                // can afford check - store
                if (player_input * 2 > store) {
                    System.out.println("\nHamurabi: Think again. You only have " + store + " bushels of grain, now then,");
                } // acre check
                else if (player_input > farm_land) {
                    System.out.println("\nHamurabi: Think again. You only have "+farm_land+" acres of land, now then,");
                } // person check
                else if (citizens > player_input * 10) {
                    System.out.println("\n“Hamurabi: Think again. You have only "+citizens+" people to tend the fields!, now then,");
                } else {
                    // transaction
                    planted = player_input;
                    store -= (player_input * 2);
                    valid_input = true;
                }
            }



            // Harvest Crops
            bushels_per_acre = random_generator.nextInt(1,7);
            int amount_harvested = bushels_per_acre * planted;

            // rats
            if (random_generator.nextInt(1,3) == 1) {
                double store_remaining_percent = 1.0 / (double) amount_harvested;
                int new_store = (int)(store * store_remaining_percent);
                rat_food = store - new_store;
                store = new_store;
            } else {
                rat_food = 0;
            }

            store += amount_harvested;



            // Population Change
            arrivals = (20*farm_land) + store;
            arrivals *= random_generator.nextInt(1,7);
            arrivals /= citizens;
            arrivals /= 100;
            arrivals += 1;

            starved = bushels_feed / 20;
            starved = citizens - starved;
            deaths += starved;
            deaths_by_starving += starved;
            proportion_starved = (double)starved / (double) citizens;



            // make the plague possible
            plague_chance = 15;



            // early exit conditions - proportion starved
            if (proportion_starved > 0.45) {
                System.out.println("You starved "+starved+" people in one year!!");
                national_Fink();
            }
        }

        // ending
        double overall_proportion_starved = (double) deaths_by_starving / (double) (citizens + deaths);
        int acres_per_person = farm_land / citizens;
        System.out.printf("""
                In your ten year term of office, %f percent of the
                population starved per year on average, i.e. a total of
                %d people died!
                
                You started with 10 acres per person and ended with
                %d acres per person.
                """, overall_proportion_starved, deaths, acres_per_person);

        // closing remarks
        if (overall_proportion_starved > 0.33 || acres_per_person < 7) {
            national_Fink();
        } else if (overall_proportion_starved > 0.1 || acres_per_person < 9) {
            System.out.println("""
                    Your heavy handed performance smacks of Nero and Ivan IV
                    The people (remaining) find you an unpleasant ruler, and
                    frankly hate your guts!!""");
            System.exit(0);
        } else if (overall_proportion_starved > 0.03 || acres_per_person < 10) {
            int assassins = random_generator.nextInt(1, (int)(citizens * 0.8));
            System.out.printf("""
                    Your performance could have been somewhat better, but
                    really wasn't too bad at all. %d people
                    would dearly like to see you assassinated but we all have
                    our trivial problems.""", assassins);
            System.exit(0);
        } else {
            System.out.println("""
                    A fantastic performance!! Charlemagne, Disraeli, and
                    Jefferson could not have done better!""");
        }
    }

    static void national_Fink() {
        System.out.print("""
                Due to this extreme mismanagement you have not only
                been impeached and thrown out of office but you have
                also been declared national fink!!!""");
        System.exit(0);
    }
}