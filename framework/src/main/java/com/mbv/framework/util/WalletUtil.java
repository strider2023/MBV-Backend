package com.mbv.framework.util;

import com.mbv.framework.props.WalletProps;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by arindamnath on 08/03/16.
 */
public class WalletUtil {

    @Autowired
    WalletProps walletProps;

    public WalletUtil() {

    }

    public WalletProps getWalletProps() {
        return walletProps;
    }

    public void setWalletProps(WalletProps walletProps) {
        this.walletProps = walletProps;
    }
}
