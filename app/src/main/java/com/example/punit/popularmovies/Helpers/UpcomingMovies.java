package com.example.punit.popularmovies.Helpers;

import java.io.Serializable;
import java.util.List;

public class UpcomingMovies implements Serializable {

    /**
     * minimum : 2015-06-24
     * maximum : 2015-07-15
     */

    private DatesEntity dates;
    /**
     * dates : {"minimum":"2015-06-24","maximum":"2015-07-15"}
     * page : 1
     * results : [{"adult":false,"backdrop_path":"/bIlYH4l2AyYvEysmS2AOfjO7Dn8.jpg","genre_ids":[878,28,53,12],"id":87101,"original_language":"en","original_title":"Terminator Genisys","overview":"The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.","release_date":"2015-07-01","poster_path":"/qOoFD4HD9a2EEUymdzBQN9XF1UJ.jpg","popularity":15.394013,"title":"Terminator Genisys","video":false,"vote_average":7.3,"vote_count":35},{"adult":false,"backdrop_path":"/g4CbTb1zMseYMqrqU4mBvjx8bGp.jpg","genre_ids":[35],"id":214756,"original_language":"en","original_title":"Ted 2","overview":"Newlywed couple Ted and Tami-Lynn want to have a baby, but in order to qualify to be a parent, Ted will have to prove he's a person in a court of law.","release_date":"2015-06-26","poster_path":"/A7HtCxFe7Ms8H7e7o2zawppbuDT.jpg","popularity":5.118746,"title":"Ted 2","video":false,"vote_average":7.3,"vote_count":38},{"adult":false,"backdrop_path":"/9scUkyWZ1oAhfOGzhFLRdQ1BuIO.jpg","genre_ids":[10751,16,12,35],"id":211672,"original_language":"en","original_title":"Minions","overview":"Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb, hatches a plot to take over the world.","release_date":"2015-07-10","poster_path":"/s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg","popularity":3.32247,"title":"Minions","video":false,"vote_average":8.3,"vote_count":41},{"adult":false,"backdrop_path":"/76q81bYJbORdYRYiasZWTMyxLP3.jpg","genre_ids":[35],"id":276844,"original_language":"en","original_title":"The Little Death","overview":"A comedy film that looks into the loosely connected lives of people with strange sexual fantasies. A woman with a dangerous fantasy and her partner's struggle to please her. A man who begins an affair with his own wife without her knowing anything about it. A couple struggling to keep things together after a sexual experiment spins out of control. A woman who can only find pleasure in her husband's pain. A call centre operator caught in the middle of a dirty and chaotic phone call. And the distractingly charming new neighbour who connects them all.","release_date":"2015-06-26","poster_path":"/eFxgujAuoCZ3mwqAjhHi5nvZ8Sd.jpg","popularity":1.656292,"title":"The Little Death","video":false,"vote_average":6,"vote_count":29},{"adult":false,"backdrop_path":"/7KV2lhasRMdwOn6wr5N1iZFbnmw.jpg","genre_ids":[10751,18,12],"id":272878,"original_language":"en","original_title":"Max","overview":"A dog that helped soldiers in Afghanistan returns to the U.S. and is adopted by his handler's family after suffering a traumatic experience.","release_date":"2015-06-26","poster_path":"/gNGAHcK1DBWYTUgKExb3MnhlJ09.jpg","popularity":2.604804,"title":"Max","video":false,"vote_average":9.8,"vote_count":2},{"adult":false,"backdrop_path":"/eWjXLMCsCXEXWZoVnNGyu4Dd72Y.jpg","genre_ids":[27,53],"id":263472,"original_language":"en","original_title":"Knock Knock","overview":"Two young women show up at the home of a married man and begin to systematically destroy his idyllic life.","release_date":"2015-06-26","poster_path":"/id9Sw7VIn97W3crPd1MIRHJ6t9Y.jpg","popularity":1.572557,"title":"Knock Knock","video":false,"vote_average":4,"vote_count":4},{"adult":false,"backdrop_path":"/b7jcdzZ4HU5yAlkfnksQHCSgpGb.jpg","genre_ids":[18,10749],"id":292431,"original_language":"en","original_title":"Love","overview":"A sexual melodrama about a boy and a girl and another girl. It's a love story, which celebrates sex in a joyous way.","release_date":"2015-07-15","poster_path":"/s3VeuIEnTgDEuUxlPAUmWItOnIt.jpg","popularity":2.507412,"title":"Love","video":false,"vote_average":7,"vote_count":1},{"adult":false,"backdrop_path":"/1DeBPZiBb0qeZ7NdzE6nGflgIKd.jpg","genre_ids":[35,18,10749],"id":283227,"original_language":"en","original_title":"A Little Chaos","overview":"A landscape gardener is hired by famous architect Le Notre to construct the grand gardens at the palace of Versailles. As the two work on the palace, they find themselves drawn to each other and are thrown into rivalries within the court of King Louis XIV.","release_date":"2015-06-26","poster_path":"/AeKFi9acqgTGZGqvBvFdMx32Cda.jpg","popularity":1.911189,"title":"A Little Chaos","video":false,"vote_average":4.4,"vote_count":5},{"adult":false,"backdrop_path":"/u7GSYEvjx0bIzoloDqj6Ea3xD1.jpg","genre_ids":[35,27],"id":323373,"original_language":"en","original_title":"Deathgasm","overview":"Two teenage boys unwittingly summon an ancient evil entity known as The Blind One by delving into black magic while trying to escape their mundane lives.","release_date":"2015-06-26","poster_path":"/yz3L43i3G6OxnXGLYJKepIX9FbM.jpg","popularity":0.836165,"title":"Deathgasm","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":null,"genre_ids":[18,27],"id":342991,"original_language":"en","original_title":"Prognosis","overview":"A woman receives a dire prognosis.","release_date":"2015-06-25","poster_path":"/jVw04CEKTfAWni7uF8KB1X4u4t7.jpg","popularity":0.784143,"title":"Prognosis","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":"/hPAf2ifJe2nQcswhtMMdedyBJ5v.jpg","genre_ids":[35,18,10402],"id":264999,"original_language":"en","original_title":"Magic Mike XXL","overview":"Three years after Mike bowed out of the stripper life at the top of his game, he and the remaining Kings of Tampa hit the road to Myrtle Beach to put on one last blow-out performance.","release_date":"2015-07-01","poster_path":"/8lBqG0KycfeSr0DBy1uauIvFEu.jpg","popularity":1.770784,"title":"Magic Mike XXL","video":false,"vote_average":7.8,"vote_count":2},{"adult":false,"backdrop_path":"/bAwGhvTqbeOu9zNoQsvGRe0MS6L.jpg","genre_ids":[27,53],"id":310137,"original_language":"en","original_title":"Reversal","overview":"A young girl, chained in the basement of a sexual predator, manages to escape and then turns the tables on her captor.","release_date":"2015-06-28","poster_path":"/n5EXevWKnakKu2aawST1p5VBs8L.jpg","popularity":0.764869,"title":"Bound to Vengeance","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":null,"genre_ids":[35,27],"id":296503,"original_language":"en","original_title":"Dude Bro Party Massacre III","overview":"In the wake of two back-to-back mass murders on Chico's frat row, loner Brent Chirino must infiltrate the ranks of a popular fraternity to investigate his twin brother's murder at the hands of the serial killer known as \"Motherface.\"","release_date":"2015-06-30","poster_path":"/4lkWO3enHPP7Ga9EPtz7pE8IB1K.jpg","popularity":0.727813,"title":"Dude Bro Party Massacre III","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":null,"genre_ids":[18,10752],"id":343843,"original_language":"en","original_title":"연평해전","overview":"The movies follows the incident knows as the second battle of Yeonpyeong which happened in 2002.","release_date":"2015-06-24","poster_path":"/1eto5bnM4m8MvA7fKLYKvmKd9vZ.jpg","popularity":0.710686,"title":"Northern Limit Line","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":"/fzHOiL0PXlXII9k2KZTKiUZlET5.jpg","genre_ids":[27],"id":299245,"original_language":"en","original_title":"The Gallows","overview":"20 years after a horrific accident during a small town school play, students at the school resurrect the failed show in a misguided attempt to honor the anniversary of the tragedy - but soon discover that some things are better left alone.","release_date":"2015-07-10","poster_path":"/8KQ0LPgGcASjJFf6qB8VtssqoRa.jpg","popularity":1.709617,"title":"The Gallows","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":"/30HAX7kZZEaCltD9tvJJmCn9vBN.jpg","genre_ids":[27],"id":312831,"original_language":"en","original_title":"The Woods","overview":"A family who move into a remote milllhouse in Ireland find themselves in a fight for survival with demonic creatures living in the woods.","release_date":"2015-06-26","poster_path":"/7NQuwtcNyHnOa7cmZEY71VPynAX.jpg","popularity":0.708282,"title":"The Woods","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":null,"genre_ids":[80,18,53],"id":200511,"original_language":"en","original_title":"7 Minutes","overview":"A young athlete takes a wild turn in life after suffering a serious injury.","release_date":"2015-06-26","poster_path":"/hc72bbafFvLGun0eAx7KjugSIIM.jpg","popularity":0.649238,"title":"7 Minutes","video":false,"vote_average":6.5,"vote_count":2},{"adult":false,"backdrop_path":null,"genre_ids":[99],"id":343880,"original_language":"en","original_title":"A Murder in the Park","overview":"Documentary filmmakers assert that Anthony Porter - a former death-row inmate who was spared the death penalty thanks to the efforts of a college journalism program - was actually guilty, and an innocent man was sent to prison.","release_date":"2015-06-26","poster_path":"/1oD2FUvNJN6SEmkNybKsEZj9APh.jpg","popularity":0.641767,"title":"A Murder in the Park","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":null,"genre_ids":[35,878],"id":86828,"original_language":"en","original_title":"Absolutely Anything","overview":"A teacher experiences a series of mishaps after discovering he has magical powers.","release_date":"2015-07-03","poster_path":"/diZK6K2wRhgvfTJ5HRPjM4liRt8.jpg","popularity":0.626652,"title":"Absolutely Anything","video":false,"vote_average":0,"vote_count":0},{"adult":false,"backdrop_path":"/nUs37LYXf8uczW1nFsyeHRp2EiE.jpg","genre_ids":[35],"id":306943,"original_language":"en","original_title":"The Outskirts","overview":"After falling victim to a humiliating prank by the high school Queen Bee, best friends and world-class geeks, Mindy and Jodi, decide to get their revenge by uniting the outcasts of the school against her and her circle of friends.","release_date":"2015-06-26","poster_path":"/6sJCmrCVVdYvcEtLzAItQwZaacv.jpg","popularity":1.622033,"title":"The Outskirts","video":false,"vote_average":0,"vote_count":0}]
     * total_pages : 5
     * total_results : 91
     */

    private int page;
    private int total_pages;
    private int total_results;
    /**
     * adult : false
     * backdrop_path : /bIlYH4l2AyYvEysmS2AOfjO7Dn8.jpg
     * genre_ids : [878,28,53,12]
     * id : 87101
     * original_language : en
     * original_title : Terminator Genisys
     * overview : The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.
     * release_date : 2015-07-01
     * poster_path : /qOoFD4HD9a2EEUymdzBQN9XF1UJ.jpg
     * popularity : 15.394013
     * title : Terminator Genisys
     * video : false
     * vote_average : 7.3
     * vote_count : 35
     */

    private List<ResultsEntity> results;

    public void setDates(DatesEntity dates) {
        this.dates = dates;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public DatesEntity getDates() {
        return dates;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class DatesEntity {
        private String minimum;
        private String maximum;

        public void setMinimum(String minimum) {
            this.minimum = minimum;
        }

        public void setMaximum(String maximum) {
            this.maximum = maximum;
        }

        public String getMinimum() {
            return minimum;
        }

        public String getMaximum() {
            return maximum;
        }
    }

    public static class ResultsEntity {
        private boolean adult;
        private String backdrop_path;
        private int id;
        private String original_language;
        private String original_title;
        private String overview;
        private String release_date;
        private String poster_path;
        private double popularity;
        private String title;
        private boolean video;
        private double vote_average;
        private int vote_count;
        private List<Integer> genre_ids;

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public void setVote_average(double vote_average) {
            this.vote_average = vote_average;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public void setGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }

        public boolean isAdult() {
            return adult;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public int getId() {
            return id;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public String getOverview() {
            return overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public double getPopularity() {
            return popularity;
        }

        public String getTitle() {
            return title;
        }

        public boolean isVideo() {
            return video;
        }

        public double getVote_average() {
            return vote_average;
        }

        public int getVote_count() {
            return vote_count;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }
    }
}
