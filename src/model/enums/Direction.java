package model.enums;

public enum Direction {
	CLOCKWISE,          
    COUNTER_CLOCKWISE;  

    
    public static Direction fromBoolean(boolean isClockwise) {
        return isClockwise ? CLOCKWISE : COUNTER_CLOCKWISE;
    }
}
