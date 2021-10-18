package com.tgcs.posbc.bridge.eft.wrapper.model;

public abstract class BanksysBeError {

    static final int OK = 0;

    public interface Transaction {
        int PAID_PARTIALLY = 1;
        int TECHNICAL_PROBLEM_WITH_SAM_OR_CHIP_CARD = 1501;
        int STOPPED_BY_SERVICE = 1502;
        int INVALID_AMOUNT_INCLUDED = 1503;
        int CARD_HAS_INVALID_IEP_HOST_ID = 1504;
        int PUR_ID_AC_PUB_PROBLEM = 1505;
        int PROTON_CARD_IN_RED_FRAME = 1506;
        int PROTON_CARD_FIXED_IN_RED_FRAME = 1507;
        int PROTON_BLOCKED_FOR_CREDIT = 1508;
        int PROTON_BLOCKED_FOR_DEBIT = 1509;
        int INSUFFICIENT_BALANCE_FOR_PROTON = 1510;
        int PROTON_CURRENCY_NOT_VALID = 1511;
        int PROTON_CARD_HAS_EXPIRED = 1512;
        int ICC_STATUS_ERROR = 1513;
        int INVALID_JOB_REQUEST = 1514;
        int ERROR_DURING_RECOVERY = 1515;
        int PROTON_IDENTIFICATION_ERROR = 1516;
        int PROTON_BALANCE_TOO_HIGH = 1517;
        int PROTON_TX_ERROR = 1518;
        int PROTON_TX_ERROR_ALT = 1519;
        int TECHNICAL_PROBLEM_TERMINAL_RESTART = 1551;

        int NO_PAPER_IN_PRINTER = 2001;
        int MANDATORY_TICKET_STILL_PENDING = 2002;

        int TIME_EXPIRED_NO_CARD_IN_READER = 4001;
        int TIME_EXPIRED_NO_CONFIRMATION = 4002;
        int TIME_EXPIRED_NO_NEW_REQUEST = 4003;
        int REQUEST_INTERRUPTED_BY_OPERATOR = 4004;
        int BALANCE_INSUFFICIENT = 4005;
        int INVALID_VALUE = 4006;
        int INVALID_MESSAGE = 4008;
        int TERMINAL_NOT_READY_TRY_AGAIN = 4009;
        int APPLICATION_NOT_SUPPORTED = 4010;
        int TIME_ELAPSED_READ_ON_CARD = 4011;

        int TECHNICAL_PROBLEM_TRANS_INTERRUPTED = 5001;
        int TRANSACTION_REFUSED_BY_HOST = 5002;
        int DOUBLE_TRANSACTION_SAME_AMOUNT_CARD = 5003;
        int TECHNICAL_PROBLEM_ON_THE_HOST = 5004;
        int TRANSACTION_CANCELED_PROBLEMS_WITH_PDV = 5005;
        int STOPPED_BY_CUSTOMER = 5006;
        int INVALID_CURRENCY = 5008;
        int TRANSACTION_REFUSED_BY_TERMINAL = 5009;
        int TELECOMMUNICATIONS_PROBLEM = 5010;
        int LIMIT_EXCEEDED = 5012;
        int TRANSACTION_REFUSED_BY_TERMINAL_ALT = 5013;
        int TRANSACTION_REFUSED_BY_CARD = 5014;
        int CODE_5015 = 5015;
        int MAX_TRANSACTIONS_PER_MONTH_REACHED = 5016;
        int MAX_UNCOLLECTED_JOURNALS_REACHED = 5017;
        int TINA_SERVICE_ALREADY_ACTIVATED = 5018;
        int TINA_SERVICE_ALREADY_Deactivated = 5019;
        int ACQUIRER_SUPPORTED_NO_SERV_ACTIVATION = 5020;
        int MAXIMUM_TRANSACTION_RECORDS_REACHED = 5021;
        int MAXIMUM_ACTIVATIONS_FOR_TINA_ACHIEVED = 5022;
        int AMOUNT_MORE_THAN_VALUE_ALLOWED = 5023;
        int CARD_PROBLEM = 5024;
        int CARD_BLOCKED = 5025;
        int CARD_REFUSED = 5026;
        int PIN_INCORRECT = 5027;
        int PRODUCT_NOT_ALLOWED = 5028;
        int PRODUCT_NOT_AVAILABLE = 5029;
        int MAP_INFO_NOT_AVAILABLE = 5030;
    }

    public interface Internal {
        int TRANSACTION_STILL_IN_PROGRESS = 800;
        int BUSY_WITH_CANCEL = 801;
        int TRAINING_NOT_ALLOWED = 802;
        int AMOUNT_TOO_HIGH = 803;
        int AMOUNT_IS_ZERO = 804;
        int TERMINAL_NOT_RESPONDING_TIMEOUT = 805;
        int INVALID_CARD_TYPE = 806;
        int CANCEL_NOT_ALLOWED = 807;
        int NOT_Supported = 808;
        int TRAINING_NOT_ALLOWED_ALT = 809;
        int ERROR_AMOUNT_TRANSACTION_STOPPED = 810;
        int CODE_811 = 811;
        int CODE_812 = 812;
        int TERMINAL_NOT_RESPONDING_TIMEOUT_ALT = 813;
        int CODE_814 = 814;
        int CODE_815 = 815;
        int CONTROLLER_SIGNATURE = 816;
        int WAITING_FOR_CARD = 817;
        int RETRIEVE_PREVIOUS_TRANSACTION = 818;
        int ERROR_AT_COLLECTION_PREVIOUS_TRANSACTION = 819;
    }

    public interface ServiceWrapper {
        int CLOSED_DEVICE = -1;
        int REPRINT_NEEDED = 851;
        int CODE_854 = 854;
        int CODE_855 = 855;
    }

    public static boolean hasNoError(int code) {
        return code == OK;
    }

    public  static boolean isTransactionError(int code) {
        return !isInternalError(code) && !isServiceWrapperError(code) && code != OK;
    }

    public static boolean isInternalError(int code) {
        return (code >= 800 && code <= 899);
    }

    public static boolean isServiceWrapperError(int code) {
        return (code == -1 || (code >= 854 && code <= 855));
    }
}
