public class Engine {
    EngineState engineState;

    public Engine(EngineState engineState) {
        this.engineState = engineState;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "engineState=" + engineState +
                '}';
    }

    public EngineState getEngineState() {
        return engineState;
    }

    public void setEngineState(EngineState engineState) {
        this.engineState = engineState;
    }
}
