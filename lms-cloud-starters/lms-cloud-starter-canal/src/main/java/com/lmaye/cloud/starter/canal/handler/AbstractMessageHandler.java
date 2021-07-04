package com.lmaye.cloud.starter.canal.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.lmaye.cloud.starter.canal.context.CanalContext;
import com.lmaye.cloud.starter.canal.model.CanalModel;
import com.lmaye.cloud.starter.canal.utils.HandlerUtil;

import java.util.List;
import java.util.Map;

/**
 * -- AbstractMessageHandler
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public abstract class AbstractMessageHandler implements MessageHandler<Message> {
    private final Map<String, EntryHandler> tableHandlerMap;

    private final RowDataHandler<CanalEntry.RowData> rowDataHandler;

    public AbstractMessageHandler(List<? extends EntryHandler> entryHandlers, RowDataHandler<CanalEntry.RowData> rowDataHandler) {
        this.tableHandlerMap = HandlerUtil.getTableHandlerMap(entryHandlers);
        this.rowDataHandler = rowDataHandler;
    }

    @Override
    public void handleMessage(Message message) {
        List<CanalEntry.Entry> entries = message.getEntries();
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType().equals(CanalEntry.EntryType.ROWDATA)) {
                try {
                    EntryHandler entryHandler = HandlerUtil.getEntryHandler(tableHandlerMap, entry.getHeader().getTableName());
                    if (entryHandler != null) {
                        CanalModel model = CanalModel.Builder.builder().id(message.getId()).table(entry.getHeader().getTableName())
                                .executeTime(entry.getHeader().getExecuteTime()).database(entry.getHeader().getSchemaName()).build();
                        CanalContext.setModel(model);
                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                        if (rowChange.getRowDatasList().size() == 0) {
                            // TODO rowChange.getIsDdl() 存在问题，需检查
                            rowDataHandler.handlerDdl(rowChange.getSql(), entryHandler);
                        }
                        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                        CanalEntry.EventType eventType = rowChange.getEventType();
                        for (CanalEntry.RowData rowData : rowDataList) {
                            rowDataHandler.handlerRowData(rowData, entryHandler, eventType);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                } finally {
                    CanalContext.removeModel();
                }
            }
        }
    }
}
