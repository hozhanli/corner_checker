package com.cin_damasi.app.chinese_checker_package;

import java.util.ArrayList;
import java.util.List;

class ComputerPlayerMinimax extends ComputerPlayer {
    private int playerToMaximize;
    private int playerToMinimize;

    private static int[][] heuristicScores = {
            {14, 13, 12, 11, 10, 9, 8, 7},
            {13, 12, 11, 10, 9, 8, 7, 6},
            {12, 11, 10, 9, 8, 7, 6, 5},
            {11, 10, 9, 8, 7, 6, 5, 4},
            {10, 9, 8, 7, 6, 5, 4, 3},
            {9, 8, 7, 6, 5, 4, 3, 2},
            {8, 7, 6, 5, 4, 3, 2, 1},
            {7, 6, 5, 4, 3, 2, 1, 0}
    };


    public ComputerPlayerMinimax(int whichPlayer) {
        super(whichPlayer);
    }

    /*
     * You will implement this function in homework
     */
    private int heuristic(GameState state, Player player, Boolean isMaximize) {
        int score = isMaximize ? 0 : 448;
        int[][] array = state.getGameStateArray();
        for (int i = 0; i < array.length; i++) {
            int count = 0;
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == player.whichPlayer) {
                    ++count;
                    score = isMaximize ? score + heuristicScores[i][j] : score - heuristicScores[i][j];
                    if (count > 3)
                        score = isMaximize ? score - 7 : score + 7;
                }
            }
        }
        for (int j = 0; j < array.length; j++) {
            int count = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i][j] == player.whichPlayer) {
                    ++count;
                    if (count > 3)
                        score = isMaximize ? score - 7 : score + 7;
                }
            }
        }

        return score;
    }


    /*
     * You will implement this function in homework
     */
    public Move getMove(GameState gameState, Player opponentPlayer) {
        List<Piece> pieces = this.pieces;

        Player currentPlayer = this;
        int depth = 5;
        Node alphaNode = Node.Alpha();
        Node betaNode = Node.Beta();
        List<Move> availableMoves = this.getAvailableMoves(gameState);
        for (Move move : availableMoves) {
            GameState updatedState = GameState.createState(gameState, move);
            Node initialNode = new Node(null, move, Integer.MIN_VALUE);
            Node nextNode = minimax(updatedState, updatePlayer(updatedState, opponentPlayer), updatePlayer(updatedState, currentPlayer), initialNode, alphaNode, betaNode, depth - 1);
            if (nextNode.root != null || nextNode.move != null) {
                alphaNode = Node.Max(alphaNode, nextNode);
            }
        }
        final Move move = alphaNode.root;
        for (Piece piece : pieces) {
            if (piece.getPosition().equals(move.getPreviousPosition())) {
                this.pieces = pieces;
                return new Move(piece, move.getPreviousPosition(), move.getNextPosition());
            }
        }
        return null;
    }

    public Node minimax(GameState state, Player current, Player opponent, Node initialNode, Node alphaNode, Node betaNode, int depth) {
        if (depth == 0)
            return initialNode;

        List<Move> moves = current.getAvailableMoves(state);
        if (moves.size() == 0) {
            return initialNode;
        }

        int score;
        if (current.whichPlayer == ProjectDefinitions.PLAYER_GREEN) {
            for (Move m : moves) {
                GameState updatedState = GameState.createState(state, m);
                score = heuristic(updatedState, current, false);
                Node nextBoard = minimax(updatedState, updatePlayer(updatedState, opponent), updatePlayer(updatedState, current), new Node(initialNode, m, score), alphaNode, betaNode, depth - 1);
                betaNode = Node.Min(betaNode, nextBoard);
                if (alphaNode.score >= betaNode.score) {
                    return alphaNode;
                }
            }
            return betaNode;
        }
        for (Move m : moves) {
            GameState updatedState = GameState.createState(state, m);
            score = heuristic(updatedState, current, true);
            Node nextBoard = minimax(updatedState, updatePlayer(updatedState, opponent), updatePlayer(updatedState, current), new Node(initialNode, m, score), alphaNode, betaNode, depth - 1);
            alphaNode = Node.Max(alphaNode, nextBoard);
            if (alphaNode.score >= betaNode.score) {
                return betaNode;
            }
        }
        return alphaNode;
    }

    private Player updatePlayer(GameState state, Player player) {
        Player p = new ComputerPlayerMinimax(player.whichPlayer);

        List<Piece> pieces = new ArrayList<>();

        int[][] array = state.getGameStateArray();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == player.whichPlayer) {
                    pieces.add(new Piece(i, j, player.whichPlayer, p));
                }
            }
        }
        player.pieces = pieces;
        return p;
    }
}


