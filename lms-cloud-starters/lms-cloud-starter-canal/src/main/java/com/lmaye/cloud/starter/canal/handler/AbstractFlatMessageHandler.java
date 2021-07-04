package com.lmaye.cloud.starter.canal.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.lmaye.cloud.starter.canal.context.CanalContext;
import com.lmaye.cloud.starter.canal.model.CanalModel;
import com.lmaye.cloud.starter.canal.utils.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * -- AbstractFlatMessageHandler
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public abstract class AbstractFlatMessageHandler implements MessageHandler<FlatMessage> {
    private final Map<String, EntryHandler> tableHandlerMap;


    private final RowDataHandler<List<Map<String, String>>> rowDataHandler;


    private Logger logger = LoggerFactory.getLogger(AbstractFlatMessageHandler.class);


    public AbstractFlatMessageHandler(List<? extends EntryHandler> entryHandlers, RowDataHandler<List<Map<String, String>>> rowDataHandler) {
        this.tableHandlerMap = HandlerUtil.getTableHandlerMap(entryHandlers);
        this.rowDataHandler = rowDataHandler;
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        List<Map<String, String>> data = flatMessage.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                CanalEntry.EventType eventType = CanalEntry.EventType.valueOf(flatMessage.getType());
                List<Map<String, String>> maps;
                if (eventType.equals(CanalEntry.EventType.UPDATE)) {
                    Map<String, String> map = data.get(i);
                    Map<String, String> oldMap = flatMessage.getOld().get(i);
                    maps = Stream.of(map, oldMap).collect(Collectors.toList());
                } else {
                    maps = Stream.of(data.get(i)).collect(Collectors.toList());
                }
                try {
                    EntryHandler entryHandler = HandlerUtil.getEntryHandler(tableHandlerMap, flatMessage.getTable());
                    if (entryHandler != null) {
                        CanalModel model = CanalModel.Builder.builder().id(flatMessage.getId()).table(flatMessage.getTable())
                                .executeTime(flatMessage.getEs()).database(flatMessage.getDatabase()).createTime(flatMessage.getTs()).build();
                        CanalContext.setModel(model);
                        rowDataHandler.handlerRowData(maps, entryHandler, eventType);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:" + data.toString(), e);
                } finally {
                    CanalContext.removeModel();
                }
            }
        }
    }
}
