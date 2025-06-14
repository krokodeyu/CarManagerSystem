package database.cms.entity;

import java.util.HashMap;
import java.util.Map;

public enum TechSpec {
    ELECTRICIAN("电工"),
    BODY_REPAIRER("钣金工"),
    PAINTER("喷漆工"),
    TIRE_TECH("轮胎工"),
    APPRENTICE("学徒工");

    private final String specName;
    public static final Map<TechSpec, Double> HOURLY_WAGE_MAP = new HashMap<>();

    static {
        // 初始化时薪映射
        HOURLY_WAGE_MAP.put(ELECTRICIAN, 50.0);      // 电工时薪 50 元
        HOURLY_WAGE_MAP.put(BODY_REPAIRER, 60.0);    // 钣金工时薪 60 元
        HOURLY_WAGE_MAP.put(PAINTER, 55.0);          // 喷漆工时薪 55 元
        HOURLY_WAGE_MAP.put(TIRE_TECH, 45.0);        // 轮胎工时薪 45 元
        HOURLY_WAGE_MAP.put(APPRENTICE, 30.0);       // 学徒工时薪 30 元
    }

    TechSpec(String specName) {
        this.specName = specName;
    }

    public static double HOURLY_WAGE_MAP(TechSpec specialization) {
        return HOURLY_WAGE_MAP.get(specialization);
    }

    public String getString() {
        return specName;
    }
}
