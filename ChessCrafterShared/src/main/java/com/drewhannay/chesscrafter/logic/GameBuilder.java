package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.*;
import com.drewhannay.chesscrafter.models.turnkeeper.ClassicTurnKeeper;
import com.drewhannay.chesscrafter.rules.Rules;
import com.drewhannay.chesscrafter.rules.endconditions.CaptureObjectiveEndCondition;
import com.drewhannay.chesscrafter.rules.legaldestinationcropper.ClassicLegalDestinationCropper;
import com.drewhannay.chesscrafter.rules.legaldestinationcropper.LegalDestinationCropper;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.drewhannay.chesscrafter.rules.promotionmethods.ClassicPromotionMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameBuilder {
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int BOTH = 3;

    // TODO: remove this eventually
    public static void main(String[] args) {
        buildClassic();
    }

    /**
     * Constructor
     *
     * @param name The name of this Game type
     */
    public GameBuilder(String name) {
        mName = name;
        mWhiteTeam = Lists.newArrayList();
        mBlackTeam = Lists.newArrayList();

        mWhitePromotionMap = Maps.newHashMap();
        mBlackPromotionMap = Maps.newHashMap();
    }

    public GameBuilder(String name, Board[] boards, List<Piece> whiteTeam, List<Piece> blackTeam, Rules whiteRules, Rules blackRules) {
        mName = name;
        mBoards = boards;

        mWhiteTeam = whiteTeam;
        mBlackTeam = blackTeam;
        mWhitePromotionMap = Maps.newHashMap();
        mBlackPromotionMap = Maps.newHashMap();

        mWhiteRules = whiteRules;
        mBlackRules = blackRules;
    }

    @NotNull
    public static Game buildClassic() {
        long pieceId = 0;

        int whiteTeamId = 1;
        int blackTeamId = 2;

        List<Piece> whitePieces = Lists.newArrayList();
        List<Piece> blackPieces = Lists.newArrayList();
        for (int i = 1; i < 9; i++) {
            whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getPawnPieceType(), ChessCoordinate.at(2, i, 0)));
            blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getPawnPieceType(), ChessCoordinate.at(7, i, 0)));
        }

        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getRookPieceType(), ChessCoordinate.at(1, 1)));
        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getKnightPieceType(), ChessCoordinate.at(1, 2)));
        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getBishopPieceType(), ChessCoordinate.at(1, 3)));
        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getQueenPieceType(), ChessCoordinate.at(1, 4)));
        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getKingPieceType(), ChessCoordinate.at(1, 5)));
        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getBishopPieceType(), ChessCoordinate.at(1, 6)));
        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getKnightPieceType(), ChessCoordinate.at(1, 7)));
        whitePieces.add(new Piece(pieceId++, whiteTeamId, PieceBuilder.getRookPieceType(), ChessCoordinate.at(1, 8)));

        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getRookPieceType(), ChessCoordinate.at(8, 1)));
        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getKnightPieceType(), ChessCoordinate.at(8, 2)));
        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getBishopPieceType(), ChessCoordinate.at(8, 3)));
        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getQueenPieceType(), ChessCoordinate.at(8, 4)));
        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getKingPieceType(), ChessCoordinate.at(8, 5)));
        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getBishopPieceType(), ChessCoordinate.at(8, 6)));
        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getKnightPieceType(), ChessCoordinate.at(8, 7)));
        blackPieces.add(new Piece(pieceId++, blackTeamId, PieceBuilder.getRookPieceType(), ChessCoordinate.at(8, 8)));

        List<ChessCoordinate> whitePromotionCoordinateList = Lists.newArrayList();
        List<ChessCoordinate> blackPromotionCoordinateList = Lists.newArrayList();
        for (int i = 1; i < 9; i++) {
            whitePromotionCoordinateList.add(ChessCoordinate.at(1, i, 0));
            blackPromotionCoordinateList.add(ChessCoordinate.at(8, i, 0));
        }

        Map<PieceType, Set<PieceType>> promotionMap = Maps.newHashMap();
        promotionMap.put(PieceBuilder.getPawnPieceType(), Sets.newHashSet(
                PieceBuilder.getRookPieceType(),
                PieceBuilder.getKnightPieceType(),
                PieceBuilder.getBishopPieceType(),
                PieceBuilder.getQueenPieceType()
        ));

        Rules whiteRules = new Rules(
                PieceBuilder.getKingPieceType(),
                whitePromotionCoordinateList,
                Rules.DESTINATION_SAME_BOARD,
                Lists.<LegalDestinationCropper>newArrayList(new ClassicLegalDestinationCropper()),
                promotionMap,
                new ClassicPromotionMethod(),
                Collections.<PostMoveAction>emptyList(),
                new CaptureObjectiveEndCondition()
        );
        Rules blackRules = new Rules(
                PieceBuilder.getKingPieceType(),
                blackPromotionCoordinateList,
                Rules.DESTINATION_SAME_BOARD,
                Lists.<LegalDestinationCropper>newArrayList(new ClassicLegalDestinationCropper()),
                promotionMap,
                new ClassicPromotionMethod(),
                Collections.<PostMoveAction>emptyList(),
                new CaptureObjectiveEndCondition()
        );

        Team[] teams = new Team[2];
        teams[0] = new Team(whiteRules, whitePieces);
        teams[1] = new Team(blackRules, blackPieces);

        Board[] boards = new Board[]{new Board(BoardSize.withDimensions(8, 8), false)};

        return new Game("Classic", boards, teams, new ClassicTurnKeeper());
    }

    public Board[] getBoards() {
        return mBoards;
    }

    public void setBoards(Board[] boards) {
        mBoards = boards;
    }

    public void setName(String name) {
        mName = name;
    }

    public void addToPromotionMap(PieceType key, List<PieceType> value, int colorCode) {
        if (colorCode == WHITE || colorCode == BOTH)
            mWhitePromotionMap.put(key, value);
        if (colorCode == BLACK || colorCode == BOTH)
            mBlackPromotionMap.put(key, value);
    }

    public void setWhitePromotionMap(Map<PieceType, List<PieceType>> promotionMap) {
        mWhitePromotionMap.clear();
        mWhitePromotionMap.putAll(promotionMap);
    }

    public void setBlackPromotionMap(Map<PieceType, List<PieceType>> promotionMap) {
        mBlackPromotionMap.clear();
        mBlackPromotionMap.putAll(promotionMap);
    }

    public Map<PieceType, List<PieceType>> getWhitePromotionMap() {
        return mWhitePromotionMap;
    }

    public Map<PieceType, List<PieceType>> getBlackPromotionMap() {
        return mBlackPromotionMap;
    }

    public Rules getWhiteRules() {
        return mWhiteRules;
    }

    public void setWhiteRules(Rules whiteRules) {
        mWhiteRules = whiteRules;
    }

    public Rules getBlackRules() {
        return mBlackRules;
    }

    public void setBlackRules(Rules blackRules) {
        mBlackRules = blackRules;
    }

    public String getName() {
        return mName;
    }

    public List<Piece> getWhiteTeam() {
        return mWhiteTeam;
    }

    public void setWhiteTeam(List<Piece> whiteTeam) {
        mWhiteTeam.clear();
        mWhiteTeam.addAll(whiteTeam);
    }

    public List<Piece> getBlackTeam() {
        return mBlackTeam;
    }

    public void setBlackTeam(List<Piece> blackTeam) {
        mBlackTeam.clear();
        mBlackTeam.addAll(blackTeam);
    }

    private final List<Piece> mWhiteTeam;
    private final List<Piece> mBlackTeam;
    private final Map<PieceType, List<PieceType>> mWhitePromotionMap;
    private final Map<PieceType, List<PieceType>> mBlackPromotionMap;

    private String mName;
    private Board[] mBoards;
    private Rules mWhiteRules;
    private Rules mBlackRules;
}
