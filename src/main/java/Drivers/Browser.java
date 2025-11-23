package Drivers;

public enum Browser {
    chrome {
        @Override
        public AbstractDriver getDriverFactory() {
            return new ChromeFactory();
        }
    },
    edge {
        @Override
        public AbstractDriver getDriverFactory() {
            return new EdgeFactory();
        }
    };

    public abstract AbstractDriver getDriverFactory();
}


