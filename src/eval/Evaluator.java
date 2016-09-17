/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eval;

import exceptions.MalformedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Madhu, Sharath Ravi
 */
public class Evaluator {
    List<String> infix, postfix;
    String result;
    
    private int getPrecedence(String Symbol) {
        switch(Symbol) {
            case "(":
            case ")":   return 10;
            case "*":
            case "/":   return 7;
            case "+":
            case "-":   return 5;
            case "%":   return 3;
            case "sqrt": return 9;
            default:    return 1;
        }
    }
    
    public boolean isOperator(String x) {
        return getPrecedence(x) > 1;
    }
    public boolean isInfixEmpty() {
        return infix.isEmpty();
    }
    
    public List<String> getInfix() {
        return infix;
    }
    
    public List<String> getPostfix() {
        return postfix;
    }
    
    public String getInfixAsString() {
        String s = "";
        for (String x : infix) {
            s = s + " " + x;
        }
        return s;
    }
    
    public Evaluator() {
        infix = new ArrayList<>();
        postfix = new ArrayList<>();
        result = "0";
    }
    
    public int getInfixLen() {
        return infix.size();
    }
    public void setInfix(String[] expression) {
        infix.addAll(Arrays.asList(expression));
    }
    public void replaceInfixTop(String term) {
        infix.remove(infix.size()-1);
        infix.add(term);
    }
    
    public boolean pushVal(String pval) {
        return infix.add(pval);
    }
    public String popInfixTop() {
       return infix.remove(infix.size() - 1);
    }
    
    public String getInfixTop() {
        if(!infix.isEmpty()) return infix.get(infix.size()-1);
        else return "0";
    }
    public String getResult() {
        return result;
    }
    
    public void clear() {
        result = "0";
        infix.clear();
        postfix.clear();
    }
    
    public void calcResult() throws MalformedException {
        toPostfix();
        evaluatePostfix();
    }
    
    public void evaluatePostfix() throws MalformedException{
        ArrayList<String> opstack;
        opstack = new ArrayList<>();
        for (String v : postfix) {
            if(getPrecedence(v) == 1) {
                opstack.add(v);
            }
            else {
                Double e1,e2,e3;
                String exp2 = opstack.remove(opstack.size()-1);
                String exp1 = opstack.remove(opstack.size()-1);
                try {
                e2 = Double.parseDouble(exp2);
                e1 = Double.parseDouble(exp1);
                e3 = 0.0;
                } catch (NumberFormatException ex) {
                    throw new MalformedException();
                }
                switch(v) {
                    case "+":   e3 = e1 + e2; break;
                    case "-":   e3 = e1 - e2; break;
                    case "*":   e3 = e1 * e2; break;
                    case "/":   e3 = e1 / e2; break;
                    case "%":   e3 = (double)e1.intValue() % e2.intValue(); break;
                    case "sqrt": e3 = Math.sqrt(e2); opstack.add(exp1); break;
                }
                opstack.add(e3.toString());
            }
        }
        if(opstack.size() > 1) throw new MalformedException();
        else
            result = opstack.remove(opstack.size()-1);
    }
    public void toPostfix() throws MalformedException {
        ArrayList<String> opstack = new ArrayList<>();
        postfix.clear();
        for(String i : infix) {
            if(getPrecedence(i) == 1) {
                postfix.add(i);
            } else {
                if(i.equals(")")) {
                    while(!opstack.isEmpty() && !"(".equals(opstack.get(opstack.size()-1))) {
                        postfix.add(opstack.remove(opstack.size()-1));
                    }
                    if(!opstack.isEmpty()) {
                        opstack.remove(opstack.size()-1);
                    }
                }else {
                    int inp = getPrecedence(i);
                    while(!opstack.isEmpty() && !"(".equals(opstack.get(opstack.size()-1)) && inp <= getPrecedence(opstack.get(opstack.size()-1)) ) {
                        postfix.add(opstack.remove(opstack.size()-1));
                    }
                    opstack.add(i);
                }
            }
            //System.out.println(infix.toString() + "   " + opstack.toString() + "   " + postfix.toString());
        }
        
        while(!opstack.isEmpty()) {
            postfix.add(opstack.remove(opstack.size()-1));
        }
        
        for (String t : postfix) {
            if(t.equals("(") || t.equals(")")) throw new MalformedException();
        }
    }
    
    public static void main(String[] args) {
        Evaluator e = new Evaluator();
        String[] exp = { "5", "+","(","4", "+", "4", "+", "6", ")", "*", "sqrt","(","8", "+" ,"8",")"};
        
        e.setInfix(exp);
        try {
            e.toPostfix();
            e.evaluatePostfix();
            System.out.println(e.getInfix().toString() + "\n" + e.getPostfix().toString() + "\nResult=" + e.getResult() );
        } catch (MalformedException ex) {
            Logger.getLogger(Evaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
