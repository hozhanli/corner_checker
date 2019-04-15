package com.cin_damasi.app.chinese_checker_package;

class Node {
    int score;
    public Move move;
    Move root = null;

    private Node(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    Node(Node root, Move move, int score) {
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

    static Node Min(Node a, Node b) {
        return a.score <= b.score ? a : b;
    }

    static Node Max(Node a, Node b) {
        return a.score >= b.score ? a : b;
    }

    static Node Alpha() {
        return new Node(null, Integer.MIN_VALUE);
    }

    static Node Beta() {
        return new Node(null, Integer.MAX_VALUE);
    }
}