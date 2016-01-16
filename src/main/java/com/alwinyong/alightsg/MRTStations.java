package com.alwinyong.alightsg;

public class MRTStations {
    public static final String[] MRT_Line_List;

    public static final String[][] NSLine;
    public static final String[][] EWLine;
    public static final String[][] CGLine;
    public static final String[][] NELine;
    public static final String[][] CCLine;
    public static final String[][] CELine;
    public static final String[][] DTLine;
    public static final String[][] BPLRT;
    public static final String[][] SELRT;
    public static final String[][] SWLRT;
    public static final String[][] PELRT;
    public static final String[][] PWLRT;
//  public static final String[][] SKLRT;
//  public static final String[][] PGLRT;

    static {

        MRT_Line_List = new String[]{
                "North South Line",
                "East West Line",
                "Changi Airport Branch Line",
                "North East Line",
                "Circle Line",
                "Circle Line Extension",
                "Downtown Line",
                "Bukit Panjang LRT",
                "Sengkang LRT (East Loop)",
                "Sengkang LRT (West Loop)",
                "Punggol LRT (East Loop)",
                "Punggol LRT (West Loop)"
        };

        NSLine = new String[][]{
                {"Jurong East", "NS1", "true", "1.3343F", "103.741942F", "2"},
                {"Bukit Batok", "NS2", "true", "1.348812F", "103.749431F", "2"},
                {"Bukit Gombak", "NS3", "true", "1.358873F", "103.751802F", "2"},
                {"Choa Chu Kang", "NS4", "true", "1.385505F", "103.744217F", "2"},
                {"Yew Tee", "NS5", "true", "1.39755F", "103.747339F", "2"},
                {"Kranji", "NS7", "true", "1.425082F", "103.761844F", "2"},
                {"Marsiling", "NS8", "true", "1.432579F", "103.774096F", "2"},
                {"Woodlands", "NS9", "true", "1.437106F", "103.786435F", "2"},
                {"Admiralty", "NS10", "true", "1.440731F", "103.800951F", "2"},
                {"Sembawang", "NS11", "true", "1.449054F", "103.820155F", "2"},
                {"Yishun", "NS13", "true", "1.429512F", "103.835165F", "2"},
                {"Khatib", "NS14", "true", "1.417403F", "103.832891F", "2"},
                {"Yio Chu Kang", "NS15", "true", "1.381912F", "103.844853F", "2"},
                {"Ang Mo Kio", "NS16", "true", "1.370049F", "103.849402F", "3"},
                {"Bishan", "NS17", "true", "1.350807F", "103.848158F", "2"},
                {"Braddell", "NS18", "false", "1.340317F", "103.845218F", "2"},
                {"Toa Payoh", "NS19", "false", "1.332723F", "103.84775F", "2"},
                {"Novena", "NS20", "false", "1.320099F", "103.84377F", "2"},
                {"Newton", "NS21", "false", "1.312515F", "103.83789F", "2"},
                {"Orchard", "NS22", "false", "1.304331F", "103.832526F", "2"},
                {"Somerset", "NS23", "false", "1.30017F", "103.838448F", "2"},
                {"Dhoby Ghaut", "NS24", "false", "1.298647F", "103.845862F", "2"},
                {"City Hall", "NS25", "false", "1.293262F", "103.852235F", "2"},
                {"Raffles Place", "NS26", "false", "1.283995F", "103.851441F", "2"},
                {"Marina Bay", "NS27", "false", "1.276143F", "103.854627F", "1"},
                {"Marina South Pier", "NS27", "false", "1.270307F", "103.862038F", "1"}
        };

        EWLine = new String[][]{
                {"Pasir Ris", "EW1", "true", "1.37258F", "103.949502F", "2"},
                {"Tampines", "EW2", "true", "1.353113F", "103.945221F", "2"},
                {"Simei", "EW3", "true", "1.343192F", "103.953332F", "2"},
                {"Tanah Merah", "EW4", "true", "1.327296F", "103.946563F", "2"},
                {"Bedok", "EW5", "true", "1.324035F", "103.93004F", "2"},
                {"Kembangan", "EW6", "true", "1.321021F", "103.912863F", "2"},
                {"Eunos", "EW7", "true", "1.319831F", "103.903079F", "2"},
                {"Paya Lebar", "EW8", "true", "1.318093F", "103.893015F", "2"},
                {"Aljunied", "EW9", "true", "1.316613F", "103.882979F", "2"},
                {"Kallang", "EW10", "true", "1.311496F", "103.871407F", "2"},
                {"Lavender", "EW11", "false", "1.307195F", "103.863006F", "2"},
                {"Bugis", "EW12", "false", "1.300824F", "103.856269F", "2"},
                {"City Hall", "EW13", "false", "1.293326F", "103.852181F", "2"},
                {"Raffles Place", "EW14", "false", "1.283909F", "103.851484F", "2"},
                {"Tanjong Pagar", "EW15", "false", "1.276454F", "103.845701F", "2"},
                {"Outram Park", "EW16", "false", "1.281324F", "103.838942F", "3"},
                {"Tiong Bahru", "EW17", "false", "1.286118F", "103.826936F", "2"},
                {"Redhill", "EW18", "true", "1.289755F", "103.816722F", "2"},
                {"Queenstown", "EW19", "true", "1.294496F", "103.806111F", "2"},
                {"Commonwealth", "EW20", "true", "1.302508F", "103.798247F", "2"},
                {"Buona Vista", "EW21", "true", "1.30741F", "103.789782F", "2"},
                {"Dover", "EW22", "true", "1.311314F", "103.778592F", "2"},
                {"Clementi", "EW23", "true", "1.315304F", "103.765267F", "2"},
                {"Jurong East", "EW24", "true", "1.334343F", "103.741953F", "2"},
                {"Chinese Garden", "EW25", "true", "1.342494F", "103.732672F", "2"},
                {"Lakeside", "EW26", "true", "1.344232F", "103.720763F", "2"},
                {"Boon Lay", "EW27", "true", "1.338858F", "103.705636F", "2"},
                {"Pioneer", "EW28", "true", "1.3374F", "103.697117F", "2"},
                {"Joo Koon", "EW29", "true", "1.327725F", "103.678449F", "2"}
        };

        CGLine = new String[][]{
                {"Tanah Merah", "EW4", "true", "1.327296F", "103.946563F", "3"},
                {"Expo", "CG1", "true", "1.334632F", "103.961454F", "6"},
                {"Changi Airport", "CG2", "false", "1.357382F", "103.988748F", "6"}
        };

        NELine = new String[][]{
                {"HarbourFront", "NE1", "false", "1.26532F", "103.820617F", "3.5"},
                {"Outram Park", "NE3", "false", "1.281388F", "103.838942F", "1.5"},
                {"Chinatown", "NE4", "false", "1.284434F", "103.84333F", "1.5"},
                {"Clarke Quay", "NE5", "false", "1.288553F", "103.846709F", "2.5"},
                {"Dhoby Ghaut", "NE6", "false", "1.298614F", "103.845894F", "1.5"},
                {"Little India", "NE7", "false", "1.30726F", "103.849821F", "1.5"},
                {"Farrer Park", "NE8", "false", "1.312451F", "103.854305F", "2.5"},
                {"Boon Keng", "NE9", "false", "1.319637F", "103.861933F", "2.5"},
                {"Potong Pasir", "NE10", "false", "1.331329F", "103.869261F", "1.5"},
                {"Woodleigh", "NE11", "false", "1.339352F", "103.870806F", "1.5"},
                {"Serangoon", "NE12", "false", "1.349198F", "103.873435F", "2.5"},
                {"Kovan", "NE13", "false", "1.360546F", "103.88514F", "2.5"},
                {"Hougang", "NE14", "false", "1.370971F", "103.892436F", "2.5"},
                {"Buangkok", "NE15", "false", "1.38248F", "103.892757F", "2.5"},
                {"Sengkang", "NE16", "false", "1.391243F", "103.895375F", "2.5"},
                {"Punggol", "NE17", "false", "1.404725F", "103.902242F", "2.5"}
        };

        CCLine = new String[][]{
                {"Dhoby Ghaut", "CC1", "false", "1.298614F", "103.845905F", "2"},
                {"Bras Basah", "CC2", "false", "1.297005F", "103.850604F", "2"},
                {"Esplanade", "CC3", "false", "1.293434F", "103.855367F", "2"},
                {"Promenade", "CC4", "false", "1.293144F", "103.861086F", "2"},
                {"Nicoll Highway", "CC5", "false", "1.299687F", "103.863596F", "2"},
                {"Stadium", "CC6", "false", "1.30283F", "103.875312F", "2"},
                {"Mountbatten", "CC7", "false", "1.306348F", "103.882501F", "2"},
                {"Dakota", "CC8", "false", "1.3083F", "103.888273F", "2"},
                {"Paya Lebar", "CC9", "false", "1.318082F", "103.892972F", "2"},
                {"MacPherson", "CC10", "false", "1.326684F", "103.889989F", "2"},
                {"Tai Seng", "CC11", "false", "1.335844F", "103.887962F", "2"},
                {"Bartley", "CC12", "false", "1.342645F", "103.879936F", "2"},
                {"Serangoon", "CC13", "false", "1.349252F", "103.873446F", "2"},
                {"Lorong Chuan", "CC14", "false", "1.352051F", "103.863532F", "3"},
                {"Bishan", "CC15", "false", "1.350839F", "103.848158F", "3"},
                {"Marymount", "CC16", "false", "1.349091F", "103.839478F", "2"},
                {"Caldecott", "CC17", "false", "1.337775F", "103.839467F", "5"},
                {"Botanic Gardens", "CC19", "false", "1.322512F", "103.815392F", "2"},
                {"Farrer Road", "CC20", "false", "1.31731F", "103.807452F", "2"},
                {"Holland Village", "CC21", "false", "1.312076F", "103.796176F", "2"},
                {"Buona Vista", "CC22", "false", "1.307421F", "103.789675F", "2"},
                {"one-north", "CC23", "false", "1.299344F", "103.7871F", "2"},
                {"Kent Ridge", "CC24", "false", "1.293402F", "103.784396F", "3"},
                {"Haw Par Villa", "CC25", "false", "1.282397F", "103.781832F", "2"},
                {"Pasir Panjang", "CC26", "false", "1.276165F", "103.791306F", "2"},
                {"Labrador Park", "CC27", "false", "1.272303F", "103.802871F", "2"},
                {"Telok Blangah", "CC28", "false", "1.270576F", "103.809663F", "2"},
                {"HarbourFront", "CC29", "false", "1.265342F", "103.820531F", "2"}
        };

        CELine = new String[][]{
                {"Marina Bay", "CE2", "false", "1.276122F", "103.854616F", "2"},
                {"Bayfront", "CE1", "false", "1.282332F", "103.859305F", "2"},
                {"Promenade", "CC4", "false", "1.293144F", "103.861086F", "2"},
                {"Nicoll Highway", "CC5", "false", "1.299687F", "103.863596F", "2"},
                {"Stadium", "CC6", "false", "1.30283F", "103.875312F", "2"},
                {"Mountbatten", "CC7", "false", "1.306348F", "103.882501F", "2"},
                {"Dakota", "CC8", "false", "1.3083F", "103.888273F", "2"},
                {"Paya Lebar", "CC9", "false", "1.318082F", "103.892972F", "2"},
                {"MacPherson", "CC10", "false", "1.326684F", "103.889989F", "2"},
                {"Tai Seng", "CC11", "false", "1.335844F", "103.887962F", "2"},
                {"Bartley", "CC12", "false", "1.342645F", "103.879936F", "2"},
                {"Serangoon", "CC13", "false", "1.349252F", "103.873446F", "2"},
                {"Lorong Chuan", "CC14", "false", "1.352051F", "103.863532F", "3"},
                {"Bishan", "CC15", "false", "1.350839F", "103.848158F", "3"},
                {"Marymount", "CC16", "false", "1.349091F", "103.839478F", "2"},
                {"Caldecott", "CC17", "false", "1.337775F", "103.839467F", "5"},
                {"Botanic Gardens", "CC19", "false", "1.322512F", "103.815392F", "2"},
                {"Farrer Road", "CC20", "false", "1.31731F", "103.807452F", "2"},
                {"Holland Village", "CC21", "false", "1.312076F", "103.796176F", "2"},
                {"Buona Vista", "CC22", "false", "1.307421F", "103.789675F", "2"},
                {"one-north", "CC23", "false", "1.299344F", "103.7871F", "2"},
                {"Kent Ridge", "CC24", "false", "1.293402F", "103.784396F", "3"},
                {"Haw Par Villa", "CC25", "false", "1.282397F", "103.781832F", "2"},
                {"Pasir Panjang", "CC26", "false", "1.276165F", "103.791306F", "2"},
                {"Labrador Park", "CC27", "false", "1.272303F", "103.802871F", "2"},
                {"Telok Blangah", "CC28", "false", "1.270576F", "103.809663F", "2"},
                {"HarbourFront", "CC29", "false", "1.265342F", "103.820531F", "2"}
        };

        DTLine = new String[][]{
                {"Bugis", "DT14", "false", "1.300717F", "103.856215F", "2"},
                {"Promenade", "DT15", "false", "1.293176F", "103.861064F", "2"},
                {"Bayfront", "DT16", "false", "1.282332F", "103.859305F", "2"},
                {"Downtown", "DT17", "false", "1.279458F", "103.852931F", "1"},
                {"Telok Ayer", "DT18", "false", "1.282125F", "103.848472F", "1"},
                {"Chinatown", "DT19", "false", "1.284445F", "103.843362F", "1"}
        };

        BPLRT = new String[][]{
                {"Choa Chu Kang", "BP1", "true", "1.385505F", "103.744217F", "2"},
                {"South View", "BP2", "true", "1.380328F", "103.745205F", "2"},
                {"Keat Hong", "BP3", "true", "1.378725F", "103.748946F", "2"},
                {"Teck Whye", "BP4", "true", "1.376725F", "103.753692F", "2"},
                {"Phoenix", "BP5", "true", "1.378657F", "103.758016F", "2"},
                {"Bukit Panjang", "BP6", "true", "1.377967F", "103.763089F", "2"},
                {"Petir", "BP7", "true", "1.377806F", "103.766670F", "2"},
                {"Pending", "BP8", "true", "1.376177F", "103.771311F", "2"},
                {"Bangkit", "BP9", "true", "1.380092F", "103.772660F", "2"},
                {"Fajar", "BP10", "true", "1.384585F", "103.770823F", "2"},
                {"Segar", "BP11", "true", "1.387813F", "103.769601F", "2"},
                {"Jelapang", "BP12", "true", "1.386714F", "103.764522F", "2"},
                {"Senja", "BP13", "true", "1.382745F", "103.762411F", "2"},
                {"Ten Mile Junction", "BP14", "true", "1.380445F", "103.760055F", "2"}
        };

        SELRT = new String[][]{
                {"Sengkang", "STC", "true", "1.391243F", "103.895375F", "2"},
                {"Compassvale", "SE1", "true", "1.394487F", "103.900448F", "2"},
                {"Rumbia", "SE2", "true", "1.391479F", "103.905971F", "2"},
                {"Bakau", "SE3", "true", "1.388004F", "103.905429F", "2"},
                {"Kangkar", "SE4", "true", "1.383889F", "103.902193F", "2"},
                {"Ranggung", "SE5", "true", "1.384022F", "103.897417F", "2"}
        };

        SWLRT = new String[][]{
                {"Sengkang", "STC", "true", "1.391243F", "103.895375F", "2"},
                {"Cheng Lim", "SW1", "true", "1.396318F", "103.893820F", "2"},
                {"Farmway", "SW2", "true", "1.397209F", "103.889289F", "2"},
                {"Kupang", "SW3", "true", "1.398223F", "103.881262F", "2"},
                {"Thanggam", "SW4", "true", "1.397318F", "103.875623F", "2"},
                {"Fernvale", "SW5", "true", "1.391943F", "103.876211F", "2"},
                {"Layar", "SW6", "true", "1.392134F", "103.880047F", "2"},
                {"Tongkang", "SW7", "true", "1.389377F", "103.885848F", "2"},
                {"Renjong", "SW8", "true", "1.386719F", "103.890523F", "2"}
        };

        PELRT = new String[][]{
                {"Punggol", "PTC", "true", "1.404725F", "103.902242F", "2"},
                {"Cove", "PE1", "true", "1.399393F", "103.905794F", "2"},
                {"Meridian", "PE2", "true", "1.396924F", "103.908903F", "2"},
                {"Coral Edge", "PE3", "true", "1.393868F", "103.912677F", "2"},
                {"Riviera", "PE4", "true", "1.394580F", "103.916174F", "2"},
                {"Kadaloor", "PE5", "true", "1.399601F", "103.916524F", "2"},
                {"Oasis", "PE6", "true", "1.402312F", "103.912740F", "2"},
                {"Damai", "PE7", "true", "1.405295F", "103.908635F", "2"}
        };

        PWLRT = new String[][]{
                {"Punggol", "PTC", "true", "1.404725F", "103.902242F", "2"},
                {"Sam Kee", "PW1", "true", "1.409707F", "103.904904F", "2"},
                {"Teck Lee", "PW2", "true", "1.412690F", "103.906553F", "2"},
                {"Punggol Point", "PW3", "true", "1.416856F", "103.906661F", "2"},
                {"Samudera", "PW4", "true", "1.415931F", "103.902202F", "2"},
                {"Nibong", "PW5", "true", "1.411926F", "103.900400F", "2"},
                {"Sumang", "PW6", "true", "1.408525F", "103.898604F", "2"},
                {"Soo Teck", "PW7", "true", "1.405417F", "103.896944F", "2"}
        };

        /*
        SKLRT = new String[][] {
                { "Sengkang",           "STC",   "true",  "1.391243F", "103.895375F", "2" },
                { "Compassvale",        "SE 1",  "true",  "1.394487F", "103.900448F", "2" },
                { "Rumbia",             "SE 2",  "true",  "1.391479F", "103.905971F", "2" },
                { "Bakau",              "SE 3",  "true",  "1.388004F", "103.905429F", "2" },
                { "Kangkar",            "SE 4",  "true",  "1.383889F", "103.902193F", "2" },
                { "Ranggung",           "SE 5",  "true",  "1.384022F", "103.897417F", "2" },
                { "Cheng Lim",          "SW 1",  "true",  "1.396318F", "103.893820F", "2" },
                { "Farmway",            "SW 2",  "true",  "1.397209F", "103.889289F", "2" },
                { "Kupang",             "SW 3",  "true",  "1.398223F", "103.881262F", "2" },
                { "Thanggam",           "SW 4",  "true",  "1.397318F", "103.875623F", "2" },
                { "Fernvale",           "SW 5",  "true",  "1.391943F", "103.876211F", "2" },
                { "Layar",              "SW 6",  "true",  "1.392134F", "103.880047F", "2" },
                { "Tongkang",           "SW 7",  "true",  "1.389377F", "103.885848F", "2" },
                { "Renjong",            "SW 8",  "true",  "1.386719F", "103.890523F", "2" }
        };
        */

        /*
        PGLRT = new String[][] {
                { "Punggol",            "PTC",   "true",  "1.404725F", "103.902242F", "2" },
                { "Cove",               "PE 1",  "true",  "1.399393F", "103.905794F", "2" },
                { "Meridian",           "PE 2",  "true",  "1.396924F", "103.908903F", "2" },
                { "Coral Edge",         "PE 3",  "true",  "1.393868F", "103.912677F", "2" },
                { "Riviera",            "PE 4",  "true",  "1.394580F", "103.916174F", "2" },
                { "Kadaloor",           "PE 5",  "true",  "1.399601F", "103.916524F", "2" },
                { "Oasis",              "PE 6",  "true",  "1.402312F", "103.912740F", "2" },
                { "Damai",              "PE 7",  "true",  "1.405295F", "103.908635F", "2" },
                { "Sam Kee",            "PW 1",  "true",  "1.409707F", "103.904904F", "2" },
                { "Teck Lee",           "PW 2",  "true",  "1.412690F", "103.906553F", "2" },
                { "Punggol Point",      "PW 3",  "true",  "1.416856F", "103.906661F", "2" },
                { "Samudera",           "PW 4",  "true",  "1.415931F", "103.902202F", "2" },
                { "Nibong",             "PW 5",  "true",  "1.411926F", "103.900400F", "2" },
                { "Sumang",             "PW 6",  "true",  "1.408525F", "103.898604F", "2" },
                { "Soo Teck",           "PW 7",  "true",  "1.405417F", "103.896944F", "2" }
        };
        */

    }
}