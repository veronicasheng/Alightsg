package com.alwinyong.alightsg;


public class LocationData {
    public static final String[] MRT_EW;
    public static final float[] MRT_EW_LAT;
    public static final float[] MRT_EW_LONG;
    public static final String[] MRT_NS;
    public static final float[] MRT_NS_LAT;
    public static final float[] MRT_NS_LONG;
    public static final String[] MRT_NE;
    public static final float[] MRT_NE_LAT;
    public static final float[] MRT_NE_LONG;
    public static final String[] MRT_CC;
    public static final float[] MRT_CC_LAT;
    public static final float[] MRT_CC_LONG;
    public static final String[] MRT_DT;
    public static final float[] MRT_DT_LAT;
    public static final float[] MRT_DT_LONG;


    static {


        MRT_EW = new String[]{
                "Aljunied",
                "Bedok",
                "Boon Lay",
                "Bugis",
                "Buona Vista",
                "Changi Airport",
                "Chinese Garden",
                "City Hall",
                "Clementi",
                "Commonwealth",
                "Dover",
                "Eunos",
                "Expo",
                "Joo Koon",
                "Jurong East",
                "Kallang",
                "Kembangan",
                "Lakeside",
                "Lavender",
                "Outram Park",
                "Pasir Ris",
                "Paya Lebar",
                "Pioneer",
                "Queenstown",
                "Raffles Place",
                "Redhill",
                "Simei",
                "Tampines",
                "Tanah Merah",
                "Tanjong Pagar",
                "Tiong Bahru"
        };

        MRT_EW_LAT = new float[]{
                1.316613F,
                1.324035F,
                1.338858F,
                1.300824F,
                1.30741F,
                1.357382F,
                1.342494F,
                1.293326F,
                1.315304F,
                1.302508F,
                1.311314F,
                1.319831F,
                1.334632F,
                1.327725F,
                1.334343F,
                1.311496F,
                1.321021F,
                1.344232F,
                1.307195F,
                1.281324F,
                1.37258F,
                1.318093F,
                1.3374F,
                1.294496F,
                1.283909F,
                1.289755F,
                1.343192F,
                1.353113F,
                1.327296F,
                1.276454F,
                1.286118F
        };

        MRT_EW_LONG = new float[]{
                103.882979F,
                103.93004F,
                103.705636F,
                103.856269F,
                103.789782F,
                103.988748F,
                103.732672F,
                103.852181F,
                103.765267F,
                103.798247F,
                103.778592F,
                103.903079F,
                103.961454F,
                103.678449F,
                103.741953F,
                103.871407F,
                103.912863F,
                103.720763F,
                103.863006F,
                103.838942F,
                103.949502F,
                103.893015F,
                103.697117F,
                103.806111F,
                103.851484F,
                103.816722F,
                103.953332F,
                103.945221F,
                103.946563F,
                103.845701F,
                103.826936F
        };

        MRT_NS = new String[]{
                "Admiralty",
                "Ang Mo Kio",
                "Bishan",
                "Braddell",
                "Bukit Batok",
                "Bukit Gombak",
                "Choa Chu Kang",
                "City Hall",
                "Dhoby Ghaut",
                "Jurong East",
                "Khatib",
                "Kranji",
                "Marina Bay",
                "Marsiling",
                "Newton",
                "Novena",
                "Orchard",
                "Raffles Place",
                "Sembawang",
                "Somerset",
                "Toa Payoh",
                "Woodlands",
                "Yew Tee",
                "Yio Chu Kang",
                "Yishun"
        };

        MRT_NS_LAT = new float[]{
                1.440731F,
                1.370049F,
                1.350807F,
                1.340317F,
                1.348812F,
                1.358873F,
                1.385505F,
                1.293262F,
                1.298647F,
                1.3343F,
                1.417403F,
                1.425082F,
                1.276143F,
                1.432579F,
                1.312515F,
                1.320099F,
                1.304331F,
                1.283995F,
                1.449054F,
                1.30017F,
                1.332723F,
                1.437106F,
                1.39755F,
                1.381912F,
                1.429512F
        };

        MRT_NS_LONG = new float[]{
                103.800951F,
                103.849402F,
                103.848158F,
                103.845218F,
                103.749431F,
                103.751802F,
                103.744217F,
                103.852235F,
                103.845862F,
                103.741942F,
                103.832891F,
                103.761844F,
                103.854627F,
                103.774096F,
                103.83789F,
                103.84377F,
                103.832526F,
                103.851441F,
                103.820155F,
                103.838448F,
                103.84775F,
                103.786435F,
                103.747339F,
                103.844853F,
                103.835165F
        };


        MRT_NE = new String[]{
                "Boon Keng",
                "Buangkok",
                "Chinatown",
                "Clarke Quay",
                "Dhoby Ghaut",
                "Farrer Park",
                "HarbourFront",
                "Hougang",
                "Kovan",
                "Little India",
                "Outram Park",
                "Potong Pasir",
                "Punggol",
                "Sengkang",
                "Serangoon",
                "Woodleigh"
        };

        MRT_NE_LAT = new float[]{
                1.319637F,
                1.38248F,
                1.284434F,
                1.288553F,
                1.298614F,
                1.312451F,
                1.26532F,
                1.370971F,
                1.360546F,
                1.30726F,
                1.281388F,
                1.331329F,
                1.404725F,
                1.391243F,
                1.349198F,
                1.339352F
        };

        MRT_NE_LONG = new float[]{
                103.861933F,
                103.892757F,
                103.84333F,
                103.846709F,
                103.845894F,
                103.854305F,
                103.820617F,
                103.892436F,
                103.88514F,
                103.849821F,
                103.838942F,
                103.869261F,
                103.902242F,
                103.895375F,
                103.873435F,
                103.870806F
        };
        MRT_CC = new String[]{
                "Bartley",
                "Bayfront",
                "Bishan",
                "Botanic Gardens",
                "Bras Basah",
                "Buona Vista",
                "Caldecott",
                "Dakota",
                "Dhoby Ghaut",
                "Esplanade",
                "Farrer Road",
                "HarbourFront",
                "Haw Par Villa",
                "Holland Village",
                "Kent Ridge",
                "Labrador Park",
                "Lorong Chuan",
                "MacPherson",
                "Marina Bay",
                "Marymount",
                "Mountbatten",
                "Nicoll Highway",
                "One North",
                "Pasir Panjang",
                "Paya Lebar",
                "Promenade",
                "Serangoon",
                "Stadium",
                "Tai Seng",
                "Telok Blangah",
        };

        MRT_CC_LAT = new float[]{
                1.342645F,
                1.282332F,
                1.350839F,
                1.322512F,
                1.297005F,
                1.307421F,
                1.337775F,
                1.3083F,
                1.298614F,
                1.293434F,
                1.31731F,
                1.265342F,
                1.282397F,
                1.312076F,
                1.293402F,
                1.272303F,
                1.352051F,
                1.326684F,
                1.276122F,
                1.349091F,
                1.306348F,
                1.299687F,
                1.299344F,
                1.276165F,
                1.318082F,
                1.293144F,
                1.349252F,
                1.30283F,
                1.335844F,
                1.270576F
        };

        MRT_CC_LONG = new float[]{
                103.879936F,
                103.859305F,
                103.848158F,
                103.815392F,
                103.850604F,
                103.789675F,
                103.839467F,
                103.888273F,
                103.845905F,
                103.855367F,
                103.807452F,
                103.820531F,
                103.781832F,
                103.796176F,
                103.784396F,
                103.802871F,
                103.863532F,
                103.889989F,
                103.854616F,
                103.839478F,
                103.882501F,
                103.863596F,
                103.7871F,
                103.791306F,
                103.892972F,
                103.861086F,
                103.873446F,
                103.875312F,
                103.887962F,
                103.809663F
        };

        MRT_DT = new String[]{
                "Bayfront",
                "Bugis",
                "Chinatown",
                "Downtown",
                "Promenade",
                "Telok Ayer"
        };

        MRT_DT_LAT = new float[]{
                1.282332F,
                1.300717F,
                1.284445F,
                1.279458F,
                1.293176F,
                1.282125F
        };

        MRT_DT_LONG = new float[]{
                103.859305F,
                103.856215F,
                103.843362F,
                103.852931F,
                103.861064F,
                103.848472F
        };
    }


}