package com.neo.config;

public class Constants {

    public static final String MQ_CONSUMER_AUTO_STARTUP = "true";

    /**
     * MQ QUEUE
     */
    public static final String MQ_QUEUE_ORDER = "ofc.order";

    /**
     * MQ EXCHANGE
     */
    public static final String MQ_EXCHANGE_CANCEL_COMPENSATION = "ofc.cancel-compensation";
    public static final String MQ_EXCHANGE_SHT_DELIVERY_WMS = "sht.wms.shtDelivery";
    public static final String MQ_EXCHANGE_ORDER = "ofc.order";
    public static final String MQ_EXCHANGE_ORDER_OWN = "ofc.order-own";
    public static final String MQ_EXCHANGE_SHT_SELL_OUTBOUND_ORDER = "yhdx.wms.shtSellOutboundOrder";
    public static final String MQ_EXCHANGE_WMS_OFC_ORDER = "yhdx.wms.ofc-order";
    public static final String MQ_EXCHANGE_OFC_UPDATE_DELIVERY_DATE = "ofc.sht.ofc-update-delivery-date";
    public static final String MQ_EXCHANGE_WMS_UPDATE_DELIVERY_DAY = "yhdx.wms.wmsUpdateDeliveryDay";
    public static final String MQ_EXCHANGE_ERP_CREATE_TRANSFER_ORDER = "sht.erp.batchCreatePurchase";
    //add work-center
    public static final String MQ_EXCHANGE_WORK_CENTER="tms.ofc.work-center";

    public static final String MQ_EXCHANGE_DISPATCHLINE_STATUS = "ofc.tms.dispatchLine.status";

    /**
     * MQ ROUTING KEY
     */
    public static final String MQ_ROUTING_KEY_CANCEL_COMPENSATION = "rkey-cancel-compensation";
    public static final String MQ_ROUTING_KEY_SHT_DELIVERY_WMS = "sht.wms.shtDelivery";
    public static final String MQ_ROUTING_KEY_ORDER = "rkey-order";
    public static final String MQ_ROUTING_KEY_ORDER_OWN = "rkey-order-own";
    public static final String MQ_ROUTING_KEY_SHT_SELL_OUTBOUND_ORDER = "yhdx.wms.shtSellOutboundOrder";
    public static final String MQ_ROUTING_KEY_WMS_OFC_ORDER = "yhdx.wms.ofc-order";
    public static final String MQ_ROUTING_KEY_OFC_UPDATE_DELIVERY_DATE = "rkey-ofc-update-delivery-date";
    public static final String MQ_ROUTING_KEY_WMS_UPDATE_DELIVERY_DAY = "yhdx.wms.wmsUpdateDeliveryDay";
    public static final String MQ_ROUTING_KEY_ERP_CREATE_TRANSFER_ORDER = "sht.erp.batchCreatePurchase";
    //add work-center
    public static final String MQ_ROUTING_KEY_WORK_CENTER="rkey-work-center";

    public static final String MQ_ROUTING_KEY_DISPATCHLINE_STATUS = "rkey-ofc.tms.dispatchLine.status";

    /**
     * MQ FAIL EXCHANGE
     */
    public static final String MQ_FAIL_EXCHANGE_ORDER = "ofc.order-fail";
    public static final String MQ_FAIL_EXCHANGE_ORDER_CENTRAL = "ofc.order-central-fail";
    public static final String MQ_FAIL_EXCHANGE_CANCEL_COMPENSATION = "ofc.cancel-compensation-fail";
    public static final String MQ_FAIL_EXCHANGE_ORDER_OWN = "ofc.order-own-fail";
    public static final String MQ_FAIL_EXCHANGE_CONFIRM_ORDER = "wms.ofc.confirm-order-fail";
    public static final String MQ_FAIL_EXCHANGE_UPDATE_DELIVERY_DATE = "sht.ofc.update-delivery-date-fail";
    public static final String MQ_FAIL_EXCHANGE_SHT_UPDATE_DELIVERY_DATE = "sht.ofc.sht-update-delivery-date-fail";
    public static final String MQ_FAIL_EXCHANGE_SHT_CENTRAL_UPDATE_DELIVERY_DATE = "sht.ofc.sht-central-update-delivery-date-fail";
    public static final String MQ_FAIL_EXCHANGE_DELIVERY_LINE = "sht.ofc.delivery-line-fail";
    public static final String MQ_FAIL_EXCHANGE_TMS_DELIVERY_LINE = "tms.ofc.delivery-line-fail";
    public static final String MQ_FAIL_EXCHANGE_PICK_UP_CODE = "sht.ofc.pickUpCode-fail";
    public static final String MQ_FAIL_EXCHANGE_WMS_UPDATE_ORDER_STATUS = "ofc.wms.wms-update-order-status-fail";
    public static final String MQ_FAIL_EXCHANGE_SERVICE_STATION_GOODS_LOCATION = "sht.ofc.service-station-goods-location-fail";
    public static final String MQ_FAIL_AUTO_ORDER = "tms.ofc.auto-delivery-fail";
    public static final String MQ_FAIL_DISPATCH_ORDER = "tms.ofc.dispatchOrder-fail";
    public static final String MQ_FAIL_DISPATCH_ORDER_LINE = "tms.ofc.dispatchOrderLine-fail";
    //add work-center
    public static final String MQ_FAIL_WORK_CENTER="tms.ofc.work-center-fail";


    /**
     * MQ FAIL ROUTING KEY
     */
    public static final String MQ_FAIL_ROUTING_KEY_ORDER = "rkey-order-fail";
    public static final String MQ_FAIL_ROUTING_KEY_ORDER_CENTRAL = "rkey-order-central-fail";
    public static final String MQ_FAIL_ROUTING_KEY_CANCEL_COMPENSATION = "rkey-cancel-compensation-fail";
    public static final String MQ_FAIL_ROUTING_KEY_ORDER_OWN = "rkey-order-own-fail";
    public static final String MQ_FAIL_ROUTING_KEY_CONFIRM_ORDER = "rkey-confirm-order-fail";
    public static final String MQ_FAIL_ROUTING_KEY_UPDATE_DELIVERY_DATE = "rkey-update-delivery-date-fail";
    public static final String MQ_FAIL_ROUTING_KEY_SHT_UPDATE_DELIVERY_DATE = "rkey-sht-update-delivery-date-fail";
    public static final String MQ_FAIL_ROUTING_KEY_SHT_CENTRAL_UPDATE_DELIVERY_DATE = "rkey-sht-central-update-delivery-date-fail";
    public static final String MQ_FAIL_ROUTING_KEY_DELIVERY_LINE = "rkey-delivery-line-fail";
    public static final String MQ_FAIL_ROUTING_KEY_TMS_DELIVERY_LINE = "rkey-tms-delivery-line-fail";
    public static final String MQ_FAIL_ROUTING_KEY_PICK_UP_CODE = "rkey-pick-up-code-fail";
    public static final String MQ_FAIL_ROUTING_KEY_WMS_UPDATE_ORDER_STATUS = "rkey-wms-update-order-status-fail";
    public static final String MQ_FAIL_ROUTING_KEY_AUTO_ORDER_STATUS = "rkey-auto-dispatch-order-fail";
    public static final String MQ_FAIL_ROUTING_KEY_DISPATCH_ORDER_STATUS = "rkey-dispatch-order-fail";
    public static final String MQ_FAIL_ROUTING_KEY_DISPATCH_ORDER_LINE_STATUS = "rkey-dispatch-order-line-fail";
    //add work-center
    public static final String MQ_FAIL_ROUTING_KEY_WORK_CENTER="rkey-work-center-fail";
    public static final String MQ_FAIL_ROUTING_KEY_SERVICE_STATION_GOODS_LOCATION = "rkey-service-station-goods-location-fail";
    public static final String ORANGE_ROUTING= "orange";
    public static final String ORANGE_QUEUE_COLOUR = "orange.queue";

    public static final String RED_ROUTING = "red";
    public static final String RED_QUEUE_COLOUR = "red.queue";

    public static String DIRECT_EXCHANGE = "direct";
    public static String RABBIT_TOPIC_EXCHANGE = "rabbit.topic.exchange";

    public static String ORANGE_QUEUE = "orange.queue";
    public static String SPEEDANDSPECIES_QUEUE = "speed.species.queue";


}
