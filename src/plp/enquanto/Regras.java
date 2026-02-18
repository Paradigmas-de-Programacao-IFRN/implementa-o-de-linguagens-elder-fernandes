package plp.enquanto;

import java.util.*;
import plp.enquanto.parser.*;

public class Regras extends EnquantoBaseVisitor<Object> {

    @Override
    public Object visitPrograma(EnquantoParser.ProgramaContext ctx) {
        List<Linguagem.Comando> comandos =
                (List<Linguagem.Comando>) visit(ctx.seqComando());
        return new Linguagem.Programa(comandos);
    }

    @Override
    public Object visitSeqComando(EnquantoParser.SeqComandoContext ctx) {
        List<Linguagem.Comando> lista = new ArrayList<>();
        for (EnquantoParser.ComandoContext cmd : ctx.comando()) {
            lista.add((Linguagem.Comando) visit(cmd));
        }
        return lista;
    }

    @Override
    public Object visitBloco(EnquantoParser.BlocoContext ctx) {
        List<Linguagem.Comando> lista =
                (List<Linguagem.Comando>) visit(ctx.seqComando());
        return new Linguagem.Bloco(lista);
    }

    @Override
    public Object visitSkip(EnquantoParser.SkipContext ctx) {
        return Linguagem.skip;
    }

    @Override
    public Object visitAtribuicao(EnquantoParser.AtribuicaoContext ctx) {
        return new Linguagem.Atribuicao(
                ctx.ID().getText(),
                (Linguagem.Expressao) visit(ctx.expressao())
        );
    }

    @Override
    public Object visitExiba(EnquantoParser.ExibaContext ctx) {
        return new Linguagem.Exiba(
                ctx.TEXTO().getText().replace("\"", "")
        );
    }

    @Override
    public Object visitEscreva(EnquantoParser.EscrevaContext ctx) {
        return new Linguagem.Escreva(
                (Linguagem.Expressao) visit(ctx.expressao())
        );
    }

    @Override
    public Object visitEnquanto(EnquantoParser.EnquantoContext ctx) {
        return new Linguagem.Enquanto(
                (Linguagem.Bool) visit(ctx.booleano()),
                (Linguagem.Comando) visit(ctx.comando())
        );
    }

    @Override
    public Object visitSe(EnquantoParser.SeContext ctx) {
        return new Linguagem.Se(
                (Linguagem.Bool) visit(ctx.booleano()),
                (Linguagem.Comando) visit(ctx.comando(0)),
                (Linguagem.Comando) visit(ctx.comando(1))
        );
    }

    // ================= EXPRESSÕES =================

    @Override
    public Object visitInt(EnquantoParser.IntContext ctx) {
        return new Linguagem.Inteiro(
                Integer.parseInt(ctx.INT().getText())
        );
    }

    @Override
    public Object visitId(EnquantoParser.IdContext ctx) {
        return new Linguagem.Id(
                ctx.ID().getText()
        );
    }

    @Override
    public Object visitLeia(EnquantoParser.LeiaContext ctx) {
        return Linguagem.leia;
    }

    @Override
    public Object visitExpPar(EnquantoParser.ExpParContext ctx) {
        return visit(ctx.expressao());
    }

    @Override
    public Object visitOpBin(EnquantoParser.OpBinContext ctx) {

        Linguagem.Expressao e1 =
                (Linguagem.Expressao) visit(ctx.expressao(0));

        Linguagem.Expressao e2 =
                (Linguagem.Expressao) visit(ctx.expressao(1));

        String op = ctx.getChild(1).getText();

        switch (op) {
            case "+": return new Linguagem.ExpSoma(e1, e2);
            case "-": return new Linguagem.ExpSub(e1, e2);
            case "*": return new Linguagem.ExpMult(e1, e2);
            case "/": return new Linguagem.ExpDiv(e1, e2);
            case "^": return new Linguagem.ExpPot(e1, e2);
        }
        throw new RuntimeException("Operador desconhecido: " + op);
    }

    @Override
    public Object visitOpRel(EnquantoParser.OpRelContext ctx) {

        Linguagem.Expressao e1 =
                (Linguagem.Expressao) visit(ctx.expressao(0));

        Linguagem.Expressao e2 =
                (Linguagem.Expressao) visit(ctx.expressao(1));

        String op = ctx.getChild(1).getText();

        switch (op) {
            case "=": return new Linguagem.ExpIgual(e1, e2);
            case "<=": return new Linguagem.ExpMenorIgual(e1, e2);
            case "<": return new Linguagem.ExpMenor(e1, e2);
            case ">": return new Linguagem.ExpMaior(e1, e2);
            case ">=": return new Linguagem.ExpMaiorIgual(e1, e2);
            case "<>": return new Linguagem.ExpDiferente(e1, e2);
        }
        throw new RuntimeException("Operador relacional desconhecido: " + op);
    }

    @Override
    public Object visitBool(EnquantoParser.BoolContext ctx) {
        return new Linguagem.Booleano(
                ctx.BOOLEANO().getText().equals("verdadeiro")
        );
    }

    @Override
    public Object visitBoolPar(EnquantoParser.BoolParContext ctx) {
        return visit(ctx.booleano());
    }

    @Override
    public Object visitNaoLogico(EnquantoParser.NaoLogicoContext ctx) {
        return new Linguagem.NaoLogico(
                (Linguagem.Bool) visit(ctx.booleano())
        );
    }

    @Override
    public Object visitELogico(EnquantoParser.ELogicoContext ctx) {
        return new Linguagem.ELogico(
                (Linguagem.Bool) visit(ctx.booleano(0)),
                (Linguagem.Bool) visit(ctx.booleano(1))
        );
    }

    @Override
    public Object visitOuLogico(EnquantoParser.OuLogicoContext ctx) {
        return new Linguagem.OuLogico(
                (Linguagem.Bool) visit(ctx.booleano(0)),
                (Linguagem.Bool) visit(ctx.booleano(1))
        );
    }

    @Override
    public Object visitXorLogico(EnquantoParser.XorLogicoContext ctx) {
        return new Linguagem.XorLogico(
                (Linguagem.Bool) visit(ctx.booleano(0)),
                (Linguagem.Bool) visit(ctx.booleano(1))
        );
    }
}
