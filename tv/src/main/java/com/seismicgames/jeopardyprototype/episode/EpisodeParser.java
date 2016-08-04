package com.seismicgames.jeopardyprototype.episode;

import com.seismicgames.jeopardyprototype.gameplay.events.AnswerReadEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.FrameZeroEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.QuestionAskedEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.WagerEvent;
import com.seismicgames.jeopardyprototype.util.TimeCode;
import com.seismicgames.jeopardyprototype.episode.QuestionInfo.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jduffy on 7/27/16.
 */
public class EpisodeParser {


    public enum GameHeaders {
        round,cat_order,played_order,type,category,clue,preface,answers,value,wager,clue_end,score_change,daily_double_reveal,wager_pause,fj_start_music,fj_end_music,fj_reveal_contestant_1,fj_reveal_contestant_2,fj_reveal_contestant_3,game_winner_announced
    }


    private static final String TAG = EpisodeParser.class.getName();

    public static EpisodeDetails parse(String questions, String meta, String marker) throws IOException {

        List<EpisodeEvent> events = new ArrayList<>();
        List<EpisodeMarker> markers = new ArrayList<>();

        //parse files
        CSVParser questionParser = CSVParser.parse(questions, getQuestionFormat());
        CSVParser metaParser = CSVParser.parse(meta, getMetaFormat());
        CSVParser markerParser = CSVParser.parse(marker, getMarkerFormat());
        CSVRecord metaRecord = metaParser.getRecords().get(0);

        //skip to first frame
        events.add(new EpisodeEvent(0, EpisodeEvent.Type.Skipped));

        //frame zero
        FrameZeroEvent fZero = new FrameZeroEvent(metaRecord.get("episode_start"));
        events.add(fZero);


        events.add(new EpisodeEvent(fZero.timestamp +100, EpisodeEvent.Type.Skipped));

        //commercials
        parseCommercials(events, metaRecord, fZero);

        //questions
        for (CSVRecord csvRecord : questionParser) {
            System.out.println(csvRecord.toString());
            parseQuestion(events, csvRecord, fZero);
        }

        //markers
        for (CSVRecord csvRecord : markerParser) {
            markers.add(new EpisodeMarker(csvRecord.get("name"), TimeCode.parse(csvRecord.get("timestamp")) + fZero.timestamp));
        }

        Collections.sort(events, new Comparator<EpisodeEvent>() {
            @Override
            public int compare(EpisodeEvent lhs, EpisodeEvent rhs) {
                return Double.compare(lhs.timestamp, rhs.timestamp);
            }
        });

//        return EpisodeDetails.getDebugDetails();
        return new EpisodeDetails(events, markers);
    }

    private static void parseCommercials(List<EpisodeEvent> events, CSVRecord metaRecord, FrameZeroEvent fZero){
        events.add(new EpisodeEvent(TimeCode.parse(metaRecord.get("commercial_break_1_start")) + fZero.timestamp, EpisodeEvent.Type.CommercialStart));
        events.add(new EpisodeEvent(TimeCode.parse(metaRecord.get("commercial_break_1_end")) + fZero.timestamp, EpisodeEvent.Type.CommercialEnd));

        events.add(new EpisodeEvent(TimeCode.parse(metaRecord.get("commercial_break_2_start")) + fZero.timestamp, EpisodeEvent.Type.CommercialStart));
        events.add(new EpisodeEvent(TimeCode.parse(metaRecord.get("commercial_break_2_end")) + fZero.timestamp, EpisodeEvent.Type.CommercialEnd));

        events.add(new EpisodeEvent(TimeCode.parse(metaRecord.get("commercial_break_3_start")) + fZero.timestamp, EpisodeEvent.Type.CommercialStart));
        events.add(new EpisodeEvent(TimeCode.parse(metaRecord.get("commercial_break_3_end")) + fZero.timestamp , EpisodeEvent.Type.CommercialEnd));

    }

    private static void parseQuestion(List<EpisodeEvent> events, CSVRecord gameRecord, FrameZeroEvent fZero) {
        QuestionType type = QuestionType.valueOf(gameRecord.get(GameHeaders.type));
        QuestionInfo q = new QuestionInfo(
                type,
                gameRecord.get(GameHeaders.category),
                gameRecord.get(GameHeaders.clue),
                gameRecord.get(GameHeaders.answers),
                type == QuestionType.FJ ? 0 : Integer.parseInt(gameRecord.get(GameHeaders.value)),
                type == QuestionType.FJ ? gameRecord.get(GameHeaders.fj_end_music) : gameRecord.get(GameHeaders.clue_end),
                type == QuestionType.FJ ? gameRecord.get(GameHeaders.fj_end_music) + 100 : gameRecord.get(GameHeaders.score_change));

        switch (type){

            case S:
                events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
                events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));
                break;
            case DD:
                events.add(new WagerEvent(TimeCode.parse(gameRecord.get(GameHeaders.wager_pause)) + fZero.timestamp, q));
                events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
                events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));
                break;
            case FJ:
                events.add(new WagerEvent(TimeCode.parse(gameRecord.get(GameHeaders.fj_start_music)) + fZero.timestamp - 100, q));
                events.add(new QuestionAskedEvent(q.readTimestamp + fZero.timestamp, q));
                events.add(new AnswerReadEvent(q.answerTimestamp + fZero.timestamp, q));
                break;
        }

    }


        private static CSVFormat getQuestionFormat(){
        return CSVFormat.EXCEL
                .withHeader(GameHeaders.class)
                //.withFirstRecordAsHeader()
                .withSkipHeaderRecord(true);
    }
    private static CSVFormat getMetaFormat(){
        return CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .withSkipHeaderRecord(true);
    }

    private static CSVFormat getMarkerFormat(){
        return CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .withSkipHeaderRecord(true);
    }
}
