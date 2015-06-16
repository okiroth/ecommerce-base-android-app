package com.mevoyalsuper.userapp.utils;

/**
 * Created by ivan on 6/1/15.
 */
public class GlobalValues {

    // TODO replace with a web call
    public static final double DELIVERY_FEE = 70.00;
    public static final String MP_MERCHANT_PUBLIC_KEY = "TEST-d9d4f827-1d2a-48de-858c-ef6445984dc8";
    public static final String MP_MERCHANT_ACCESS_TOKEN = "TEST-3088349074981973-061016-55e106480a5f4bbbaadddce1778052aa__LB_LD__-95244956";

    public static final String WEB_BASE = "http://db.supermagico.com";
    public static final String WEB_BASE_API = WEB_BASE + "/api";

    // ITEMS page
    public static final String ALL_CATEGORIES = "Todo";
    public static final String LIMIT_MAX_GIRD_ITEMS = "500";

    public static final int SEARCH_MIN_LENGTH = 3;
    // FRAGMENTS CODES
    public static final int FRAG_ADDRESS    = 1;
    public static final int FRAG_USERDATA   = 2;
    public static final int FRAG_CREDIT     = 3;

    public static final int FRAG_NULL       = 0;



    // STATUS
    public static int STATUS_NEW = 0;
    public static int STATUS_PROCESSING = 1;
    public static int STATUS_ON_ROUTE = 3;

    public static int STATUS_DELIVER_DONE = 10;
    public static int STATUS_DELIVER_CONFIREMD = 11;
    public static int STATUS_DELIVER_ATTEMP = 12;

    public static int STATUS_CANCELED = 20;


    // PAYMENT
    public static int PAYMENT_PENDING = 0;
    public static int PAYMENT_DONE = 2;
    public static int PAYMENT_EXECUTE = 10;
}
