package database.cms.entity;

public enum TechSpec {
    ELECTRICIAN("电工"),
    BODY_REPAIRER("钣金工"),
    PAINTER("喷漆工"),
    TIRE_TECH("轮胎工"),
    APPRENTICE("学徒工");

    private final String specName;

    TechSpec(String specName) {
        this.specName = specName;
    }

    public String getString() {
        return specName;
    }
}
