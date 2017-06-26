package br.com.wemind.marketplacetribanco.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.wemind.marketplacetribanco.models.Listable;

public class BrazilianStates {
    private static final List<StateListable> LIST = new ArrayList<>();

    static {
        LIST.addAll(Arrays.asList(
                new StateListable("AC"),
                new StateListable("AL"),
                new StateListable("AM"),
                new StateListable("AP"),
                new StateListable("CE"),
                new StateListable("DF"),
                new StateListable("ES"),
                new StateListable("GO"),
                new StateListable("MA"),
                new StateListable("MG"),
                new StateListable("MS"),
                new StateListable("MT"),
                new StateListable("PA"),
                new StateListable("PB"),
                new StateListable("PE"),
                new StateListable("PI"),
                new StateListable("PR"),
                new StateListable("RJ"),
                new StateListable("RN"),
                new StateListable("RO"),
                new StateListable("RR"),
                new StateListable("RS"),
                new StateListable("SC"),
                new StateListable("SE"),
                new StateListable("SP"),
                new StateListable("TO")
        ));
    }

    public static List<StateListable> getList() {
        return new ArrayList<>(LIST);
    }

    public static class StateListable implements Listable {
        private String label;

        private StateListable(String label) {
            this.label = label;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }
}
