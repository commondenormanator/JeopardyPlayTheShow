package com.seismicgames.jeopardyprototype.episode;

import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jduffy on 6/30/16.
 */
public class EpisodeDetails {

    public List<EpisodeEvent> events = new ArrayList<>();

    public EpisodeDetails(){
        events.add(new EpisodeEvent(0, EpisodeEvent.Type.Skipped));
        events.add(new EpisodeEvent(/*116000*/200000, EpisodeEvent.Type.EpisodeStart));
        events.add(new EpisodeEvent(220000, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(224000, EpisodeEvent.Type.AnswerRead));


        events.add(new EpisodeEvent(220433, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(224000, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(231667, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(234367, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(241267, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(246267, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(254000, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(257033, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(264600, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(268033, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(275967, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(278433, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(285500, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(289233, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(296100, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(299467, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(307633, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(310833, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(321533, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(324433, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(332033, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(334267, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(345367, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(348200, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(356167, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(359567, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(369967, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(373333, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(382367, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(389900, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(636500, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(639300, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(650900, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(657467, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(667267, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(673367, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(682100, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(685200, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(693300, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(695467, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(706833, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(710600, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(718767, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(730800, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(744000, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(746800, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(758900, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(761867, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(770433, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(774400, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(780467, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(783033, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(790867, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(794067, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(841133, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(844333, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1041800, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1044433, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1053067, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1056067, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1065200, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1067933, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1086700, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1090133, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1096767, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1100767, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1109800, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1112933, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1120000, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1124367, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1134900, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1137533, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1145767, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1148567, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1184700, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1187933, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1197767, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1206200, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1216367, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1224567, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1230733, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1242200, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1253133, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1255733, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1264200, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1267033, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1272500, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1275667, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1280933, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1284267, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1295767, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1298600, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1305567, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1308167, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1318600, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1321333, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1364667, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1373800, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1384100, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1386867, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1392800, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1395767, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1403467, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1406267, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1410633, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1413300, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1420533, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1422500, EpisodeEvent.Type.AnswerRead));
        events.add(new EpisodeEvent(1428333, EpisodeEvent.Type.QuestAsked));
        events.add(new EpisodeEvent(1432033, EpisodeEvent.Type.AnswerRead));

    }




}
