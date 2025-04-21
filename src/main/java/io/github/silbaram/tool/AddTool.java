package io.github.silbaram.tool;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;


public class AddTool {
//    private static final ObjectMapper MAPPER = new ObjectMapper();

    /* 1) 툴 정의(JSON Schema 포함) */
    public McpSchema.Tool definition() {
        String schemaJson = """
            {
              "type": "object",
              "properties": {
                "a": { "type": "number", "description": "첫 번째 수" },
                "b": { "type": "number", "description": "두 번째 수" }
              },
              "required": ["a", "b"]
            }""";

        return new McpSchema.Tool(
                "add",
                "두 숫자를 더해 합계를 반환합니다.",
                schemaJson
        );
    }

    /* 2) 실제 합계를 계산해 CallToolResult 로 래핑 */
    private McpSchema.CallToolResult doAdd(Map<String, Object> args) {
        double a = ((Number) args.get("a")).doubleValue();
        double b = ((Number) args.get("b")).doubleValue();
        String sum = String.valueOf(a + b);

        // 문자열 결과를 TextContent 로 감싸 CallToolResult 생성
        return McpSchema.CallToolResult.builder()
                .addTextContent(sum)
                .build();
    }
    /* 3) 서버에 넘길 SyncToolSpecification */
    public McpServerFeatures.SyncToolSpecification registration() throws Exception {
        return new McpServerFeatures.SyncToolSpecification(
                definition(),
                (McpSyncServerExchange exchange, Map<String, Object> argMap) -> doAdd(argMap)
        );
    }
}
