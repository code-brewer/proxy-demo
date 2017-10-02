package com.fangminx.chain;

import java.util.Arrays;
import java.util.List;

/**
 * 把所有handler存放在Chain里，并在Chain里提供递归方法来遍历所有handler，handler目标方法执行完后回到Chain中执行下一条
 */
public class ChainClient {
    static class ChainHandlerA extends ChainHandler{
        @Override
        protected void handleProcess() {
            System.out.println("handle by chain a");
        }
    }
    static class ChainHandlerB extends ChainHandler{
        @Override
        protected void handleProcess() {
            System.out.println("handle by chain b");
        }
    }
    static class ChainHandlerC extends ChainHandler{
        @Override
        protected void handleProcess() {
            System.out.println("handle by chain c");
        }
    }

    public static void main(String[] args){
        List<ChainHandler> handlers = Arrays.asList(
                new ChainHandlerA(),
                new ChainHandlerB(),
                new ChainHandlerC()
        );
        Chain chain = new Chain(handlers);
        chain.proceed();
    }
}
