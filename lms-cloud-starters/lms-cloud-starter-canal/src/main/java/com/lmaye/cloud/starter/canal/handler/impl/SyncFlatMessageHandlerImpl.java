package com.lmaye.cloud.starter.canal.handler.impl;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.lmaye.cloud.starter.canal.handler.AbstractFlatMessageHandler;
import com.lmaye.cloud.starter.canal.handler.EntryHandler;
import com.lmaye.cloud.starter.canal.handler.RowDataHandler;

import java.util.List;
import java.util.Map;

/**
 * -- SyncFlatMessageHandlerImpl
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class SyncFlatMessageHandlerImpl extends AbstractFlatMessageHandler {
    public SyncFlatMessageHandlerImpl(List<? extends EntryHandler> entryHandlers,
                                      RowDataHandler<List<Map<String, String>>> rowDataHandler) {
        super(entryHandlers, rowDataHandler);
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        super.handleMessage(flatMessage);
    }
}
