package capital.scalable.restdocs.jackson.snippet;

import static capital.scalable.restdocs.jackson.OperationAttributeHelper.getHandlerMethod;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.web.method.HandlerMethod;

public abstract class StandardTableSnippet extends TemplatedSnippet {
    protected StandardTableSnippet(String snippetName, Map<String, Object> attributes) {
        super(snippetName, attributes);
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        HandlerMethod handlerMethod = getHandlerMethod(operation);

        List<FieldDescriptor> fieldDescriptors = emptyList();
        if (handlerMethod != null) {
            fieldDescriptors = createFieldDescriptors(operation, handlerMethod);
        }

        return createModel(handlerMethod, fieldDescriptors);
    }

    protected abstract List<FieldDescriptor> createFieldDescriptors(Operation operation,
            HandlerMethod handlerMethod);

    protected void enrichModel(Map<String, Object> model, HandlerMethod handlerMethod) {
        // can be used to add additional fields
    }

    private Map<String, Object> createModel(HandlerMethod handlerMethod,
            List<FieldDescriptor> fieldDescriptors) {
        Map<String, Object> model = new HashMap<>();
        enrichModel(model, handlerMethod);

        List<Map<String, Object>> fields = new ArrayList<>();
        model.put("content", fields);
        for (FieldDescriptor descriptor : fieldDescriptors) {
            fields.add(createModelForDescriptor(descriptor));
        }
        model.put("hasContent", !fieldDescriptors.isEmpty());
        model.put("noContent", fieldDescriptors.isEmpty());
        return model;
    }

    protected Map<String, Object> createModelForDescriptor(FieldDescriptor descriptor) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("path", descriptor.getPath());
        model.put("type", stringOrEmpty(descriptor.getType()));
        model.put("optional", descriptor.isOptional());
        model.put("description", stringOrEmpty(descriptor.getDescription()));
        return model;
    }

    private String stringOrEmpty(Object value) {
        if (value != null) {
            return value.toString();
        } else {
            return "";
        }
    }
}