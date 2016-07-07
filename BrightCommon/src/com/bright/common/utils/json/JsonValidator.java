package com.bright.common.utils.json;

import android.text.TextUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * 判断是否是有效的Json
 */
public class JsonValidator {
    private CharacterIterator it;
    private char c;
    private int col;

    public JsonValidator() {
    }

    public boolean validate(String jsonStr) {
        jsonStr = jsonStr.trim();
        if (TextUtils.isEmpty(jsonStr)) {
            return false;
        }
        boolean ret = this.valid(jsonStr);
        return ret;
    }

    private boolean valid(String input) {
        if ("".equals(input)) {
            return true;
        } else {
            boolean ret = true;
            this.it = new StringCharacterIterator(input);
            this.c = this.it.first();
            this.col = 1;
            if (!this.value()) {
                ret = this.error("value", 1);
            } else {
                this.skipWhiteSpace();
                if (this.c != '\uffff') {
                    ret = this.error("end", this.col);
                }
            }

            return ret;
        }
    }

    private boolean value() {
        return this.literal("true") || this.literal("false") || this.literal("null") || this.string() || this.number() || this.object() || this.array();
    }

    private boolean literal(String text) {
        StringCharacterIterator ci = new StringCharacterIterator(text);
        char t = ci.first();
        if (this.c != t) {
            return false;
        } else {
            int start = this.col;
            boolean ret = true;

            for (t = ci.next(); t != '\uffff'; t = ci.next()) {
                if (t != this.nextCharacter()) {
                    ret = false;
                    break;
                }
            }

            this.nextCharacter();
            if (!ret) {
                this.error("literal " + text, start);
            }

            return ret;
        }
    }

    private boolean array() {
        return this.aggregate('[', ']', false);
    }

    private boolean object() {
        return this.aggregate('{', '}', true);
    }

    private boolean aggregate(char entryCharacter, char exitCharacter, boolean prefix) {
        if (this.c != entryCharacter) {
            return false;
        } else {
            this.nextCharacter();
            this.skipWhiteSpace();
            if (this.c == exitCharacter) {
                this.nextCharacter();
                return true;
            } else {
                while (true) {
                    if (prefix) {
                        int start = this.col;
                        if (!this.string()) {
                            return this.error("string", start);
                        }

                        this.skipWhiteSpace();
                        if (this.c != 58) {
                            return this.error("colon", this.col);
                        }

                        this.nextCharacter();
                        this.skipWhiteSpace();
                    }

                    if (!this.value()) {
                        return this.error("value", this.col);
                    }

                    this.skipWhiteSpace();
                    if (this.c != 44) {
                        if (this.c == exitCharacter) {
                            this.nextCharacter();
                            return true;
                        }

                        return this.error("comma or " + exitCharacter, this.col);
                    }

                    this.nextCharacter();
                    this.skipWhiteSpace();
                }
            }
        }
    }

    private boolean number() {
        if (!Character.isDigit(this.c) && this.c != 45) {
            return false;
        } else {
            int start = this.col;
            if (this.c == 45) {
                this.nextCharacter();
            }

            if (this.c == 48) {
                this.nextCharacter();
            } else {
                if (!Character.isDigit(this.c)) {
                    return this.error("number", start);
                }

                while (Character.isDigit(this.c)) {
                    this.nextCharacter();
                }
            }

            if (this.c == 46) {
                this.nextCharacter();
                if (!Character.isDigit(this.c)) {
                    return this.error("number", start);
                }

                while (Character.isDigit(this.c)) {
                    this.nextCharacter();
                }
            }

            if (this.c == 101 || this.c == 69) {
                this.nextCharacter();
                if (this.c == 43 || this.c == 45) {
                    this.nextCharacter();
                }

                if (!Character.isDigit(this.c)) {
                    return this.error("number", start);
                }

                while (Character.isDigit(this.c)) {
                    this.nextCharacter();
                }
            }

            return true;
        }
    }

    private boolean string() {
        if (this.c != 34) {
            return false;
        } else {
            int start = this.col;
            boolean escaped = false;
            this.nextCharacter();

            for (; this.c != '\uffff'; this.nextCharacter()) {
                if (!escaped && this.c == 92) {
                    escaped = true;
                } else if (escaped) {
                    if (!this.escape()) {
                        return false;
                    }

                    escaped = false;
                } else if (this.c == 34) {
                    this.nextCharacter();
                    return true;
                }
            }

            return this.error("quoted string", start);
        }
    }

    private boolean escape() {
        int start = this.col - 1;
        return " \\\"/bfnrtu".indexOf(this.c) < 0 ? this.error("escape sequence  \\\",\\\\,\\/,\\b,\\f,\\n,\\r,\\t  or  \\uxxxx ", start) : (this.c != 117 || this.ishex(this.nextCharacter()) && this.ishex(this.nextCharacter()) && this.ishex(this.nextCharacter()) && this.ishex(this.nextCharacter()) ? true : this.error("unicode escape sequence  \\uxxxx ", start));
    }

    private boolean ishex(char d) {
        return "0123456789abcdefABCDEF".indexOf(this.c) >= 0;
    }

    private char nextCharacter() {
        this.c = this.it.next();
        ++this.col;
        return this.c;
    }

    private void skipWhiteSpace() {
        while (Character.isWhitespace(this.c)) {
            this.nextCharacter();
        }

    }

    private boolean error(String type, int col) {
        System.out.printf("type: %s, col: %s%s", new Object[]{type, Integer.valueOf(col), System.getProperty("line.separator")});
        return false;
    }
}

