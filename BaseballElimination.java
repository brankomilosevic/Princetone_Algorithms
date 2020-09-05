/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     2020-04-13
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BaseballElimination {

    private HashMap<String, Integer> mTeams;
    private String[] mRTeams;
    private int[] mWins;
    private int[] mLoses;
    private int[] mRemain;
    private int[][] mGames;
    private int mNumberOfTeams;
    private int mMaxWins;
    private String mMaxTeam;
    private int prev;
    private int third;
    private FordFulkerson mMaxflow;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        In read = new In(filename);

        this.mNumberOfTeams = read.readInt();
        mTeams = new HashMap<String, Integer>();
        mRTeams = new String[mNumberOfTeams];
        mWins = new int[mNumberOfTeams];
        mLoses = new int[mNumberOfTeams];
        mRemain = new int[mNumberOfTeams];
        mGames = new int[mNumberOfTeams][mNumberOfTeams];
        mMaxWins = -1;
        prev = -1;
        for (int i = 0; i < mNumberOfTeams; i++) {
            String str = read.readString();
            mTeams.put(str, i);
            mWins[i] = read.readInt();
            mRTeams[i] = str;
            if (mMaxWins < mWins[i]) mMaxTeam = str;
            mMaxWins = Math.max(mMaxWins, mWins[i]);
            mLoses[i] = read.readInt();
            mRemain[i] = read.readInt();
            for (int j = 0; j < mNumberOfTeams; j++) {
                mGames[i][j] = read.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return this.mNumberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return this.mTeams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!this.mTeams.containsKey(team)) {
            throw new IllegalArgumentException("No team found!!!");
        }
        return this.mWins[mTeams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!this.mTeams.containsKey(team)) {
            throw new IllegalArgumentException("No team found!!!");
        }
        return this.mLoses[this.mTeams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!this.mTeams.containsKey(team)) {
            throw new IllegalArgumentException("No team found!!!");
        }
        return this.mRemain[this.mTeams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!this.mTeams.containsKey(team1)
                || !this.mTeams.containsKey(team2)) {
            throw new IllegalArgumentException("No team found!!!");
        }
        int index1 = mTeams.get(team1);
        int index2 = mTeams.get(team2);
        return mGames[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!mTeams.containsKey(team)) {
            throw new IllegalArgumentException("No team found!!!");
        }
        int index = mTeams.get(team);

        if (mMaxWins > this.mWins[index] + this.mRemain[index]) return true;

        int comp = 0;
        int total = 0;
        for (int i = 0; i < this.mNumberOfTeams; i++) {
            if (i != index) {
                for (int j = i + 1; j < this.mNumberOfTeams; j++) {
                    if (j == index) continue;
                    if (mGames[i][j] > 0) {
                        total += mGames[i][j];
                        comp++;
                    }
                }
            }
        }

        if (prev == index) {
            if ((int) mMaxflow.value() < total) return true;
            else return false;
        }

        prev = index;
        int vert = 1 + comp + (this.mNumberOfTeams - 1) + 1;
        int source = 0;
        int sink = vert - 1;

        FlowNetwork net = new FlowNetwork(vert);

        int counter = 0;
        third = comp + 1;

        for (int i = 0; i < this.mNumberOfTeams; i++) {
            if (i == index) continue;
            for (int j = i + 1; j < this.mNumberOfTeams; j++) {
                if (j == index) continue;
                if (mGames[i][j] > 0) {
                    counter++;
                    net.addEdge(new FlowEdge(source, counter, mGames[i][j]));
                    int t1;
                    int t2;
                    if (i < index) t1 = third + i;
                    else t1 = third + i - 1;
                    if (j < index) t2 = third + j;
                    else t2 = third + j - 1;
                    net.addEdge(new FlowEdge(counter, t1, Double.POSITIVE_INFINITY));
                    net.addEdge(new FlowEdge(counter, t2, Double.POSITIVE_INFINITY));
                }
            }
        }

        for (int i = 0; i < mNumberOfTeams; i++) {
            if (i == index) continue;
            int from;
            if (i < index) from = third + i;
            else from = third + i - 1;
            int wei = mWins[index] + mRemain[index] - mWins[i];
            if (wei > 0)
                net.addEdge(new FlowEdge(from, sink, wei));
        }

        mMaxflow = new FordFulkerson(net, source, sink);
        if ((int) mMaxflow.value() < total) return true;

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!mTeams.containsKey(team))
            throw new IllegalArgumentException("No team found!!!");

        if (!this.isEliminated(team)) return null;

        int index = mTeams.get(team);
        Set<String> result = new HashSet<String>();

        if (mMaxWins > this.mWins[index] + this.mRemain[index]) {
            result.add(mMaxTeam);
            return result;
        }

        for (int i = 0; i < this.mNumberOfTeams; i++) {
            if (i == index) continue;
            int vertex;
            if (i < index) vertex = third + i;
            else vertex = third + i - 1;
            if (mMaxflow.inCut(vertex)) {
                result.add(mRTeams[i]);
            }
        }

        return result;
    }

    // test unit
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                System.out.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    System.out.print(t + " ");
                }
                System.out.println("}");
            }
            else {
                System.out.println(team + " is not eliminated");
            }
        }
    }
}
