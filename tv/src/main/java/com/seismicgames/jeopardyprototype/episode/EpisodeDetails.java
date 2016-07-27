package com.seismicgames.jeopardyprototype.episode;

import com.seismicgames.jeopardyprototype.gameplay.events.AnswerReadEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.FrameZeroEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.QuestionAskedEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.WagerEvent;
import com.seismicgames.jeopardyprototype.util.TimeCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jduffy on 6/30/16.
 */
public class EpisodeDetails {

    public List<EpisodeEvent> events = new ArrayList<>();

    public EpisodeDetails(){

        List<QuestionInfo> questions = new ArrayList<>();


        questions.add(new QuestionInfo("SHERWOOD FOREST","REMEMBER THE SHERIFF WHO BATTLED ROBIN HOOD & IT MAKES SENSE THAT SHERWOOD FOREST IS IN THIS BRITISH SHIRE","NOTTINGHAMSHIRE|NOTTINGHAM",200,"01:01:44;13", "01:01:48;00"));
        questions.add(new QuestionInfo("SHERWOOD FOREST","THE 800-YEAR-OLD \"MAJOR\" ONE OF THESE IN THE FOREST IS STILL PRODUCING ACORNS","OAK",400,"01:01:55;20", "01:01:58;11"));
        questions.add(new QuestionInfo("SHERWOOD FOREST","THE CLUMBER TYPE OF THIS DOG GETS ITS NAME FROM AN ESTATE IN SHERWOOD FOREST","SPANIEL",600,"01:02:05;08", "01:02:10;08"));
        questions.add(new QuestionInfo("SHERWOOD FOREST","SHERWOOD TIMBER WAS USED IN THIS CATHEDRAL BUILT IN LONDON BETWEEN 1675 & 1710","ST. PAUL'S",800,"01:02:18;00", "01:02:21;01"));
        questions.add(new QuestionInfo("SHERWOOD FOREST","SHERWOOD FOREST WAS A FAVORITE HUNTING GROUND OF THIS FIRST STUART KING OF ENGLAND","JAMES I|JAMES THE FIRST",1000,"01:02:28;18", "01:02:32;01"));
        questions.add(new QuestionInfo("ROBIN HOOD","IN \"ROBIN HOOD: PRINCE OF THIEVES\" CHRISTIAN SLATER WAS WILL SCARLETT TO HIS ROBIN HOOD","KEVIN COSTNER",200,"01:02:39;29", "01:02:42;13"));
        questions.add(new QuestionInfo("ALAN OR DALE","HE DIED IN 2001 DUE TO INJURIES SUFFERED DURING THE DAYTONA 500","DALE EARNHARDT",200,"01:02:49;15", "01:02:53;07"));
        questions.add(new QuestionInfo("ALAN OR DALE","HIS REIGN AS THE HEAD OF THE FED LASTED FROM 1987 TO 2006","ALAN GREENSPAN",400,"01:03:00;03", "01:03:03;14"));
        questions.add(new QuestionInfo("ALAN OR DALE","THIS PERSONALITY FIRST WON FRIENDS & INFLUENCED PEOPLE WHILE TEACHING PUBLIC SPEAKING AT THE YMCA","DALE CARNEGIE",600,"01:03:11;19", "01:03:14;25"));
        questions.add(new QuestionInfo("ROBIN HOOD","ROBIN AND THE 7 HOODS RETOLD THE LEGEND AS A GANGSTER STORY, WITH DEAN MARTIN AS LITTLE JOHN & THIS CROONER AS ROBBO","FRANK SINATRA",400,"01:03:25;16", "01:03:28;13"));
        questions.add(new QuestionInfo("ROBIN HOOD","HERE'S THIS ACTOR WHO PLAYED ROBIN HOOD MAKING MERRY WITH ONE OF HIS MEN DURING A BREAK IN FILMING","ERROL FLYNN",600,"01:03:36;01", "01:03:38;08"));
        questions.add(new QuestionInfo("FRYER TUCK","TUCK MAKES HIS OWN FRIED THIS SOFT DRINK DUBBED \"MOST CREATIVE\" AT THE TEXAS STATE FAIR; HAVE ONE & A SMILE","COKE|COCA-COLA",200,"01:03:49;11", "01:03:52;06"));
        questions.add(new QuestionInfo("MAID, MARRYIN'","AFTER ARTHUR BECOMES KING LEODEGRANCE'S SON-IN-LAW, THE KING GIVES HIM THIS, WHICH WILL SEAT 150","ROUND TABLE",200,"01:04:00;05", "01:04:03;17"));
        questions.add(new QuestionInfo("MAID, MARRYIN'","IN THIS NOVEL, MOLLY BLOOM THINKS BACK ON HER HUSBAND'S PROPOSAL: \"THEN HE ASKED ME WOULD I YES TO SAY YES MY MOUNTAIN FLOWER\"","ULYSSES",400,"01:04:13;29", "01:04:17;10"));
        questions.add(new QuestionInfo("MAID, MARRYIN'","IN A CHRISTOPHER MARLOWE POEM, A \"PASSIONATE\" MAN WITH THIS JOB SAYS, \"COME LIVE WITH ME AND BE MY LOVE\"","SHEPHERD",600,"01:04:26;11", "01:04:33;27"));
        questions.add(new QuestionInfo("ALAN OR DALE","THE CAPTCHA TEST, AGAINST SPAM & ROBOT PROGRAMS, IS CALLED THE REVERSE TEST NAMED FOR THIS BRITISH CODEBREAKER","ALAN TURING",800,"01:08:40;15", "01:08:43;09"));
        questions.add(new QuestionInfo("ALAN OR DALE","A 1976 CAR ACCIDENT LEFT THIS AMERICAN ARTIST BLIND IN ONE EYE; HE THEN USED ASSISTANT GLASSBLOWERS TO EXECUTE HIS DESIGNS","DALE CHIHULY",1000,"01:08:54;27", "01:09:01;14"));
        questions.add(new QuestionInfo("ROBIN HOOD","THE 2010 FILM OF \"ROBIN HOOD\" STARRED HIM IN THE TITLE ROLE, AS AN ARCHER IN THE ARMY WHO BECOMES THE LEGENDARY HERO","RUSSELL CROWE",800,"01:09:11;08", "01:09:17;11"));
        questions.add(new QuestionInfo("ROBIN HOOD","VIEWERS WERE SHAKEN & STIRRED BY HIS PORTRAYAL OF THE OLDER BUT WISER TITLE HERO IN \"ROBIN AND MARIAN\"","SEAN CONNERY",1000,"01:09:26;03", "01:09:29;06"));
        questions.add(new QuestionInfo("FRYER TUCK","CHECK OUT TUCK'S DEEP-FRYING OF THIS HIGH-PROTEIN SOY ITEM... Y'KNOW, 'CAUSE IT'S HEALTHY","TOFU|BEAN CURD",400,"01:09:37;09", "01:09:39;14"));
        questions.add(new QuestionInfo("MAID, MARRYIN'","IN THIS GARCIA MARQUEZ BOOK WITH \"LOVE\" IN THE TITLE, FERMINA WILL WED FLORENTINO IF HE NEVER MAKES HER EAT EGGPLANT","LOVE IN THE TIME OF CHOLERA",800,"01:09:50;25", "01:09:54;18"));
        questions.add(new QuestionInfo("LITTLE, JOHN","THIS HYPHENATED TERM FOR SOMEONE WHO'S SMALL CONTAINS A LIQUID MEASURE","PINT-SIZE|HALF-PINT",600,"01:10:02;23", "01:10:14;24"));
        questions.add(new QuestionInfo("FRYER TUCK","IN 2011 CAROUSEL FOODS WOWED TUCK, PUTTING THIS ITEM, FRIED, ATOP A CHEESEBURGER; NO WORD IF IT WAS CHERRY GARCIA","ICE CREAM",600,"01:10:28;00", "01:10:30;24"));
        questions.add(new QuestionInfo("FRYER TUCK","TUCK GROANS, \"ARR, ME HEART-IE!\" EATING A BRUNCH BURGER (FRIED EGG, BEEF, BACON & CHEESE IN A DONUT) AT PNC PARK IN THIS CITY","PITTSBURGH",800,"01:10:42;27", "01:10:45;26"));
        questions.add(new QuestionInfo("MAID, MARRYIN'","THIS CHAUCER GAL TELLS SOON-TO-BE HUBBY NO. 5 SHE'D MARRY HIM IF SHE WERE A WIDOW","WIFE OF BATH|ALISON",1000,"01:10:54;13", "01:10:58;12"));
        questions.add(new QuestionInfo("LITTLE, JOHN","IT PRECEDES \"WILLIE WINKIE\" IN A NURSERY RHYME","WEE",200,"01:11:04;14", "01:11:07;01"));
        questions.add(new QuestionInfo("LITTLE, JOHN","A LAND CREATED BY JONATHAN SWIFT GAVE US THIS WORD MEANING TINY","LILLIPUTIAN",1000,"01:11:14;26", "01:11:18;02"));
//        questions.add(new QuestionInfo("LITTLE, JOHN","BEGINNING WITH A WORD THAT MEANS IMMEASURABLY GREAT, IT MEANS EXCEEDINGLY SMALL","INFINITESIMAL",,"01:11:41;04", "01:11:55;00"));
        questions.add(new QuestionInfo("FRYER TUCK","TUCK GOES FULL FANNIE FLAGG MAKING THIS 3-WORD TITLE DISH, DIPPING 'EM IN FLOUR, MILK, EGGS & BREAD CRUMBS","FRIED GREEN TOMATOES",1000,"01:12:05;04", "01:12:08;10"));
        questions.add(new QuestionInfo("TRIBUTE ALBUMS","A 1995 ALBUM, \"COME TOGETHER: AMERICA SALUTES\" THIS GROUP, FEATURED 17 OF THEIR SONGS AS SUNG BY COUNTRY MUSIC STARS","BEATLES",400,"01:15:25;24", "01:15:28;13"));
        questions.add(new QuestionInfo("TRIBUTE ALBUMS","THIS SINGER'S GUITARIST RECORDED A TRIBUTE ALBUM CONTAINING SUCH TRACKS AS \"ONLY THE LONELY\" & \"IN DREAMS\"","ROY ORBISON",800,"01:15:37;02", "01:15:40;02"));
        questions.add(new QuestionInfo("TRIBUTE ALBUMS","COMMON THREAD, A TRIBUTE TO THIS GROUP, INCLUDED \"DESPERADO\" BY CLINT BLACK & \"LYIN' EYES\" BY DIAMOND RIO","EAGLES",1200,"01:15:49;06", "01:15:51;28"));
        questions.add(new QuestionInfo("SCIENCE","Ca, CALCIUM HAS 2 ELECTRONS IN ITS OUTER SHELL; WHEN THEY ARE LOST, Ca 2+ IS FORMED; IT'S ONE OF THESE CHARGED ATOMS...ARGON ATOM","ION|CATION",400,"01:16:10;21", "01:16:14;04"));
        questions.add(new QuestionInfo("SCIENCE","AN OSTEOBLAST IS A CELL THAT PROVIDES THE COLLAGEN NEEDED FOR A NEW ONE OF THESE","BONE",800,"01:16:20;23", "01:16:24;23"));
        questions.add(new QuestionInfo("TRIBUTE ALBUMS","THE FLAMING LIPS' \"AFTER THE GOLD RUSH\" IS ON \"THE BRIDGE: A TRIBUTE TO\" THIS BRIDGE SCHOOL BENEFACTOR","NEIL YOUNG",1600,"01:16:33;24", "01:16:36;28"));
        questions.add(new QuestionInfo("TRIBUTE ALBUMS","THE DOWNLOAD \"OKX\", WITH \"KARMA POLICE\", WAS A TRIBUTE TO THIS BAND","RADIOHEAD",2000,"01:16:44;00", "01:16:48;11"));
        questions.add(new QuestionInfo("EUROPEAN RULERS","LUDOVICO MANIN, THE LAST DOGE OF THIS REPUBLIC, SERVED FROM 1789 TO 1797, WHEN HE WAS DEPOSED","VENICE|VENETIAN REPUBLIC",400,"01:16:58;27", "01:17:01;16"));
        questions.add(new QuestionInfo("EUROPEAN RULERS","IN 1913 GEORGE I, KING OF THIS COUNTRY, WAS ASSASSINATED AT SALONIKA ","GREECE",800,"01:17:09;23", "01:17:12;17"));
//        questions.add(new QuestionInfo("EUROPEAN RULERS","THIS FRENCH KING'S ATTEMPT TO ESCAPE HIS FATE IS KNOWN AS THE FLIGHT TO VARENNES","LOUIS XVI|LOUIS THE SIXTEENTH",,"01:17:32;01", "01:17:38;14"));
        questions.add(new QuestionInfo("EUROPEAN RULERS","THIS COUNTRY'S AFONSO V, A NEPHEW OF HENRY THE NAVIGATOR, WAS CALLED \"THE AFRICAN\" FOR HIS MOROCCO BATTLES","PORTUGAL",1600,"01:17:48;21", "01:17:51;28"));
        questions.add(new QuestionInfo("EUROPEAN RULERS","THE WORDS FOR \"KING\" IN CZECH, KRAL, & IN POLISH, KROL, COME FROM THE NAME OF THIS RULER WHO FOUGHT THE SLAVS","CHARLEMAGNE|CHARLES THE GREAT",2000,"01:18:01;23", "01:18:10;06"));
        questions.add(new QuestionInfo("THE MUSEUM OF SOMETHING","OF THIS, ON NYC's 53rd STREET SINCE 1932","MODERN ART|MUSEUM OF MODERN ART",400,"01:18:20;11", "01:18:28;17"));
        questions.add(new QuestionInfo("O SAY CAN YOU \"C\"","PERTAINING TO THE CARE OF WOMEN IN CHILDBIRTH","OBSTETRIC",400,"01:18:34;22", "01:18:46;06"));
        questions.add(new QuestionInfo("CANADIAN PROVINCIAL FLAGS","THIS CEREAL GRAIN IS DEPICTED ON THE FLAGS OF THE PRAIRIE PROVINCES ALBERTA & SASKATCHEWAN","WHEAT",400,"01:18:57;04", "01:18:59;22"));
        questions.add(new QuestionInfo("CANADIAN PROVINCIAL FLAGS","THIS MAMMAL OF THE PLAINS THAT PROVIDED FOOD & CLOTHING FOR INDIGENOUS PEOPLES IS DEPICTED ON MANITOBA'S FLAG","BISON|BUFFALO",800,"01:19:08;06", "01:19:11;01"));
        questions.add(new QuestionInfo("CANADIAN PROVINCIAL FLAGS","THE WAVY BLUE BARS ON ITS FLAG REPRESENT THE PACIFIC OCEAN","BRITISH COLUMBIA",1200,"01:19:16;15", "01:19:19;20"));
        questions.add(new QuestionInfo("CANADIAN PROVINCIAL FLAGS","NOVA SCOTIA'S FLAG FEATURES THIS CROSS OF SCOTLAND","CROSS OF ST. ANDREW|SALTIRE",1600,"01:19:24;28", "01:19:28;08"));
        questions.add(new QuestionInfo("CANADIAN PROVINCIAL FLAGS","THIS PROVINCE HAS A 2-PART NAME & THE 2 WHITE TRIANGLES ON ITS FLAG REPRESENT THE MAINLAND & ISLAND REACHING FORWARD TOGETHER","NEWFOUNDLAND & LABRADOR",2000,"01:19:39;23", "01:19:42;18"));
        questions.add(new QuestionInfo("O SAY CAN YOU \"C\"","THIS WORD DESCRIBES CERTAIN GREEK DEITIES OR CERTAIN LARGE POOLS","OLYMPIC",800,"01:19:49;17", "01:19:52;05"));
        questions.add(new QuestionInfo("SCIENCE","THE \"SKIN EFFECT\" IN THIS MOVEMENT OF ELECTRICITY THROUGH A MEDIUM IS WHY A HARDTOP IS SAFER IN A LIGHTNING STRIKE THAN A CONVERTIBLE","CONDUCTION|CONDUCTIVITY",1200,"01:20:02;18", "01:20:05;10"));
//        questions.add(new QuestionInfo("SCIENCE","DISEASE-SPECIFIC GENES HAVE BEEN FOUND THAT COULD HELP TELL PSORIASIS FROM THIS SKIN AFFLICTION, AIDING IN TREATMENT","ECZEMA|DERMATITIS",,"01:20:29;24", "01:20:41;00"));
        questions.add(new QuestionInfo("THE MUSEUM OF SOMETHING","OF THESE \"ARTS\", FEATURING STIEGLITZ & WEEGEE","PHOTOGRAPHIC",800,"01:20:48;20", "01:20:57;24"));
        questions.add(new QuestionInfo("SCIENCE","IT WAS SOON AFTER THE BIG BANG THAT LIGHTER ELEMENTS FORMED, LIKE HYDROGEN, HELIUM & THIS NEXT ONE ON THE PERIODIC TABLE","LITHIUM",2000,"01:21:08;03", "01:21:10;26"));
        questions.add(new QuestionInfo("O SAY CAN YOU \"C\"","A LOATHSOME TYPE OF CREATURE SERVING IN THE ARMIES OF MORDOR","ORC",1200,"01:21:16;24", "01:21:19;23"));
        questions.add(new QuestionInfo("O SAY CAN YOU \"C\"","LOOK, THE SECOND CRANIAL NERVE IS ALSO CALLED THIS NERVE","OPTIC|OPHTHALMOLOGIC",2000,"01:21:27;14", "01:21:30;08"));
        questions.add(new QuestionInfo("O SAY CAN YOU \"C\"","RELATING TO ENTITIES HAVING A CARBON MAKEUP","ORGANIC",1600,"01:21:34;19", "01:21:37;09"));
        questions.add(new QuestionInfo("THE MUSEUM OF SOMETHING","WITH A PORTRAIT OF MRS. CEZANNE, A FORMER TRAIN STATION, LE MUSEE D'...","ORSAY|D'ORSAY",1200,"01:21:44;16", "01:21:46;15"));
        questions.add(new QuestionInfo("THE MUSEUM OF SOMETHING","IN BOZEMAN; MOR FOR SHORT","ROCKIES|MUSEUM OF THE ROCKIES",2000,"01:21:52;10", "01:21:56;01"));
//        questions.add(new QuestionInfo("TELEVISION","SET TO THE SONG \"YOU'VE GOT TIME\", A MONTAGE OF REAL WOMEN WHO WERE INCARCERATED IS IN THE OPENING CREDITS OF THIS SERIES","ORANGE IS THE NEW BLACK",,));



        events.add(new EpisodeEvent(3000, EpisodeEvent.Type.Skipped));
        FrameZeroEvent fZero = new FrameZeroEvent("00:58:04;09");
        events.add(fZero);
        events.add(new EpisodeEvent(fZero.timestamp + 100, EpisodeEvent.Type.Skipped));

        //temp: quick skip to first question
//        events.add(new EpisodeEvent(questions.get(0).readTimestamp + fZero.timestamp - 10000, EpisodeEvent.Type.EpisodeStart));

        for (QuestionInfo q : questions) {
            events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
            events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));
        }

        QuestionInfo q;
        q = new QuestionInfo("LITTLE, JOHN","BEGINNING WITH A WORD THAT MEANS IMMEASURABLY GREAT, IT MEANS EXCEEDINGLY SMALL","INFINITESIMAL",0,"01:11:41;04", "01:11:55;00");
        events.add(new WagerEvent(TimeCode.parse("01:11:29;12") + fZero.timestamp));
        events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));


//        events.add(new EpisodeEvent(0, EpisodeEvent.Type.Skipped));


        q = new QuestionInfo("EUROPEAN RULERS","THIS FRENCH KING'S ATTEMPT TO ESCAPE HIS FATE IS KNOWN AS THE FLIGHT TO VARENNES","LOUIS XVI|LOUIS THE SIXTEENTH",0,"01:17:32;01", "01:17:38;14");
        events.add(new WagerEvent(TimeCode.parse("01:17:22;25") + fZero.timestamp));
        events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));



//        events.add(new EpisodeEvent(0, EpisodeEvent.Type.Skipped));

        q = new QuestionInfo("SCIENCE","DISEASE-SPECIFIC GENES HAVE BEEN FOUND THAT COULD HELP TELL PSORIASIS FROM THIS SKIN AFFLICTION, AIDING IN TREATMENT","ECZEMA|DERMATITIS",0,"01:20:29;24", "01:20:41;00");
        events.add(new WagerEvent(TimeCode.parse("01:20:15;08") + fZero.timestamp));
        events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));


//        events.add(new EpisodeEvent(fZero.timestamp + 100, EpisodeEvent.Type.Skipped));

        q = new QuestionInfo("TELEVISION","SET TO THE SONG \"YOU'VE GOT TIME\", A MONTAGE OF REAL WOMEN WHO WERE INCARCERATED IS IN THE OPENING CREDITS OF THIS SERIES","ORANGE IS THE NEW BLACK",0,"01:25:59;07", "01:26:29;21");
        events.add(new WagerEvent(TimeCode.parse("01:25:59;07") + fZero.timestamp));
        events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));

//        QuestionInfo q;
//
//        q = new QuestionInfo("LITTLE, JOHN","IT PRECEDES \"WILLIE WINKIE\" IN A NURSERY RHYME","WEE",200,"01:11:04;14", "01:11:07;01");
//        events.add(new EpisodeEvent(q.readTimestamp + fZero.timestamp, EpisodeEvent.Type.QuestAsked));
//        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));
//
//        q = new QuestionInfo("LITTLE, JOHN","A LAND CREATED BY JONATHAN SWIFT GAVE US THIS WORD MEANING TINY","LILLIPUTIAN",1000,"01:11:14;26", "01:11:18;02");
//        events.add(new EpisodeEvent(q.readTimestamp + fZero.timestamp, EpisodeEvent.Type.QuestAsked));
//        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));
//
//        //daily double
//        q = new QuestionInfo("LITTLE, JOHN","BEGINNING WITH A WORD THAT MEANS IMMEASURABLY GREAT, IT MEANS EXCEEDINGLY SMALL","INFINITESIMAL",0,"01:11:41;04", "01:11:55;00");
//        events.add(new WagerEvent(TimeCode.parse("01:11:29;12") + fZero.timestamp));
//        events.add(new EpisodeEvent(q.readTimestamp + fZero.timestamp, EpisodeEvent.Type.QuestAsked));
//        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));
//
//        q = new QuestionInfo("FRYER TUCK","TUCK GOES FULL FANNIE FLAGG MAKING THIS 3-WORD TITLE DISH, DIPPING 'EM IN FLOUR, MILK, EGGS & BREAD CRUMBS","FRIED GREEN TOMATOES",1000,"01:12:05;04", "01:12:08;10");
//        events.add(new EpisodeEvent(q.readTimestamp + fZero.timestamp, EpisodeEvent.Type.QuestAsked));
//        events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));


        Collections.sort(events, new Comparator<EpisodeEvent>() {
            @Override
            public int compare(EpisodeEvent lhs, EpisodeEvent rhs) {
                return Double.compare(lhs.timestamp, rhs.timestamp);
            }
        });
    }




}
