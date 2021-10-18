package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.common.XmlDocument;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class NgpHandlerFactory implements ApplicationContextAware, ChecRequestType {

    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    public IHandler getHandler(XmlDocument node) {
        if (node == null) {
            return null;
        }

        switch (node.getRootNodeName()) {
            case INITIALIZATION:
                return get(InitializationHandler.class);
            case QUERY_STATUS:
                return get(QueryStatusHandler.class);
            case REPORT_STATUS:
                return get(ReportStatusHandler.class);
            case ADD_ITEM:
                return get(AddItemHandler.class);
            case SIGN_OFF:
                return get(SignOffHandler.class);
            case TERMINATE:
                return get(TerminateHandler.class);
            case SUSPEND_TRANSACTION:
                return get(SuspendTransactionHandler.class);
            case VOID_TRANSACTION:
                return get(VoidTransactionHandler.class);
            case PRINT_CURRENT_RECEIPTS:
                return get(PrintCurrentReceiptsHandler.class);
            case GET_TOTALS:
                return get(GetTotalsHandler.class);
            case ADD_TENDER:
                return get(AddTenderHandler.class);
            case ADD_COUPON:
                return get(AddCouponHandler.class);
            case ADD_CUSTOMER:
                return get(AddCustomerHandler.class);
            case ADD_CUSTOMER_BIRTHDATE:
                return get(AddCustomerBirthdateHandler.class);
            case ADD_RECEIPT_LINES:
                return get(AddReceiptLinesHandler.class);
            case REPRINT_RECEIPTS:
                return get(ReprintReceiptsHandler.class);
            default:
                return get(UnsupportedOperationHandler.class);

        }
    }

    private <T> T get(Class<T> beanClass) {
        return appContext.getBean(beanClass);
    }

}
