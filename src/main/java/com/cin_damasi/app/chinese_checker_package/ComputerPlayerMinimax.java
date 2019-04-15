package com.cin_damasi.app.chinese_checker_package;

import java.util.ArrayList;
import java.util.List;

class ComputerPlayerMinimax extends ComputerPlayer {
    public ComputerPlayerMinimax(int whichPlayer) {
        super(whichPlayer);
    }

    /*
     * You will implement this function in homework
     */
    private int calculateStateHeuristicValue_1(GameState state) {
        return 0;
    }

    /*
     * You will implement this function in homework
     */
    private int calculateStateHeuristicValue_2(GameState state) {
        return 0;
    }


    /*
     * You will implement this function in homework
     */
    public Move getMove(GameState gameState, Player opponentPlayer) {
        Player currentPlayer = this;
        int depth = 3;
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
        final Move move = alphaNode.move;
        for (Piece piece : this.pieces) {
            if (piece.getPosition().equals(move.getPreviousPosition())) {
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

        int score = 0;
        if (current.whichPlayer == ProjectDefinitions.PLAYER_GREEN) {
            for (Move m : moves) {
                GameState updatedState = GameState.createState(state, m);
                score = (int) (Math.random() * (100 - 1)) + 1;
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
            score = (int) (Math.random() * (99 - 1)) + 1;
            Node nextBoard = minimax(updatedState, updatePlayer(updatedState, opponent), updatePlayer(updatedState, current), new Node(initialNode, m, score), alphaNode, betaNode, depth - 1);
            alphaNode = Node.Max(alphaNode, nextBoard);
            if (alphaNode.score >= betaNode.score) {
                return betaNode;
            }
        }
        return alphaNode;
    }

    private Player updatePlayer(GameState state, Player player) {
        List<Piece> pieces = new ArrayList<>();
        int[][] array = state.getGameStateArray();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == player.whichPlayer) {
                    pieces.add(new Piece(i, j, player.whichPlayer, player));
                }
            }
        }
        player.pieces = pieces;
        return player;
    }
}


class Node {
    public int score;
    public Move move;
    public Move root = null;

    public Node(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    public Node(Node root, Move move, int score) {
        if (root != null) {
            if (root.root != null) {
                this.root = root.root;
            } else {
                this.root = root.move;
            }
        }
        this.move = move;
        this.score = score;
    }

    public static Node Min(Node a, Node b) {
        return a.score <= b.score ? a : b;
    }

    public static Node Max(Node a, Node b) {
        return a.score >= b.score ? a : b;
    }

    public static Node Alpha() {
        return new Node(null, Integer.MIN_VALUE);
    }

    public static Node Beta() {
        return new Node(null, Integer.MAX_VALUE);
    }
}