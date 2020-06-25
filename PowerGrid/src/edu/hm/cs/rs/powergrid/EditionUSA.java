
package edu.hm.cs.rs.powergrid;

import java.util.List;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-07
 */
public class EditionUSA extends EditionStandard {
    @Override public List<String> getCitySpecifications() {
        return List.of("Boston 1",
                               "New-York 1 Boston 3",
                               "Philadelphia 1 New-York 0",
                               "Norfolk 3",
                               "Washington 1 Philadelphia 3 Norfolk 5",
                               "Buffalo 1 New-York 8",
                               "Raleigh 3 Norfolk 3",
                               "Pittsburg 1 Buffalo 7 Washington 6 Raleigh 7",
                               "Miami 3",
                               "Savannah 3 Raleigh 7",
                               "Jacksonville 3 Savannah 0",
                               "Tampa 3 Jacksonville 4 Miami 4",
                               "Detroit 1 Buffalo 7 Pittsburg 6",
                               "Atlanta 3 Raleigh 7 Savannah 7",
                               "Knoxville 2 Atlanta 5",
                               "Cincinnati 2 Detroit 4 Pittsburg 7 Raleigh 15 Knoxville 6",
                               "Birmingham 4 Atlanta 3 Jacksonville 9",
                               "Chicago 2 Detroit 7 Cincinnati 7",
                               "Memphis 4 Birmingham 6",
                               "St-Louis 2 Chicago 10 Cincinnati 12 Atlanta 12 Memphis 7",
                               "New-Orleans 4 Memphis 7 Birmingham 11 Jacksonville 16",
                               "Duluth 2 Detroit 15 Chicago 12",
                               "Minneapolis 2 Duluth 5 Chicago 8",
                               "Kansas-City 4 Chicago 8 St-Louis 6 Memphis 12",
                               "Omaha 5 Minneapolis 8 Chicago 13 Kansas-City 5",
                               "Houston 4 New-Orleans 8",
                               "Dallas 4 Memphis 12 New-Orleans 12 Houston 5",
                               "Fargo 2 Duluth 6 Minneapolis 6",
                               "Oklahoma-City 4 Kansas-City 8 Memphis 14 Dallas 3",
                               "Cheyenne 5 Minneapolis 18 Omaha 14",
                               "Denver 5 Cheyenne 0 Kansas-City 16",
                               "Santa-Fe 6 Denver 13 Kansas-City 16 Oklahoma-City 15 Dallas 16 Houston 21",
                               "Billings 5 Fargo 17 Minneapolis 18 Cheyenne 9",
                               "Phoenix 6 Santa-Fe 18",
                               "Salt-Lake-City 6 Denver 21 Santa-Fe 28",
                               "Las-Vegas 6 Salt-Lake-City 18 Santa-Fe 27 Phoenix 15",
                               "Boise 5 Billings 12 Cheyenne 24 Salt-Lake-City 8",
                               "San-Diego 6 Las-Vegas 9 Phoenix 14",
                               "Los-Angeles 6 Las-Vegas 9 San-Diego 3",
                               "Seattle 5 Billings 9 Boise 12",
                               "San-Francisco 6 Boise 23 Salt-Lake-City 27 Las-Vegas 14 Los-Angeles 9",
                               "Portland 5 Seattle 3 Boise 13 San-Francisco 24"
                               );
    }
}
