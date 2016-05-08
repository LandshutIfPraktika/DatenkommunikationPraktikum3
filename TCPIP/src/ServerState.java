/**
 * Created by Georg on 07.05.2016.
 */
public enum ServerState {


    Init {
        @Override
        public ServerState process(String input) {
            final ServerState newState;
            switch (input) {
                case Commands.GO:
                    newState = SNext;
                    break;
                default:
                    newState = Init;
                    break;
            }
            return newState;
        }

        @Override
        public String getAnswer(ServerState newState) {
            final String answer;
            switch (newState) {
                case SNext:
                    answer = Constants.GONE;
                    break;
                default:
                    answer = null;
                    break;
            }
            return answer;
        }

    }, SNext {
        @Override
        public ServerState process(String input) {
            final ServerState newState;
            switch (input) {
                case Commands.RIGHT:
                    newState = SRight;
                    break;
                case Commands.LEFT:
                    newState = SLeft;
                    break;
                default:
                    newState = SNext;
                    break;
            }
            return newState;
        }

        @Override
        public String getAnswer(ServerState newState) {
            final String answer;
            switch (newState) {
                case SRight:
                    answer = Constants.WENT_RIGHT;
                    break;
                case SLeft:
                    answer = Constants.WENT_LEFT;
                    break;
                default:
                    answer = null;
                    break;
            }
            return answer;
        }


    }, SLeft {
        @Override
        public ServerState process(String input) {

            final ServerState newState;
            switch (input) {
                case Commands.BACK:
                    newState = Init;
                    break;
                default:
                    newState = SLeft;
                    break;
            }
            return newState;
        }

        @Override
        public String getAnswer(ServerState newState) {
            final String answer;
            switch (newState) {
                case Init:
                    answer = Constants.WENT_BACK;
                    break;
                default:
                    answer = null;
                    break;
            }
            return answer;
        }

    }, SRight {
        @Override
        public ServerState process(String input) {
            final ServerState newState;
            switch (input) {
                case Commands.ONCE_MORE:
                    newState = SNext;
                    break;
                default:
                    newState = SRight;
                    break;
            }
            return newState;
        }

        @Override
        public String getAnswer(ServerState newState) {
            final String answer;
            switch (newState) {
                case SNext:
                    answer = Constants.DID_ONCE_MORE;
                    break;
                default:
                    answer = null;
                    break;
            }
            return answer;
        }

    };

    abstract public ServerState process(String input);

    abstract public String getAnswer(ServerState newState);

    private static class Constants {
        public static final String GONE = "Gone";
        public static final String WENT_RIGHT = "WentRight";
        public static final String WENT_LEFT = "WentLeft";
        public static final String WENT_BACK = "WentBack";
        public static final String DID_ONCE_MORE = "DidOnceMore";
    }
}
