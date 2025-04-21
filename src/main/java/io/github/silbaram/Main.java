package io.github.silbaram;

import io.github.silbaram.tool.AddTool;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

public class Main {

    public static void main(String[] args) {

        /* 1) StdioServerTransportProvider 생성  */
        var transportProvider = new StdioServerTransportProvider();   // stdin / stdout

        /* 2) ‘add’ 툴 준비  */
        AddTool addTool = new AddTool();

        /* 3) 동기식 MCP 서버 빌드  */
        McpSyncServer syncServer = McpServer
            .sync(transportProvider)                                                    // 필수
            .serverInfo(new McpSchema.Implementation("mcp-add", "1.0.0")) // 메타
            .capabilities(McpSchema.ServerCapabilities.builder()
                .resources(true, true)
                .tools(true)
                .prompts(true)
                .logging()
                .build())
            .tools(addTool.registration())                                              // 툴 등록
            .build();

        /* 4) JVM이 종료될 때 우아하게 종료  */
        Runtime.getRuntime().addShutdownHook(new Thread(syncServer::closeGracefully));
    }
}