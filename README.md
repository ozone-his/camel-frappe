# Camel Frappe Component

This is a Camel component that provides integration with Frappe. It allows you to interact with [Frappe's REST API](https://frappeframework.com/docs/user/en/api/rest).

## URI format

```
frappe://<operation>/resource?options
```

Where `operation` is one of the following:

- `get`
- `post`
- `put`
- `delete`

`options` are the query parameters that you can pass to the Frappe API. For example, to get a list of items from the `Item` resource, you can use the following URI:

```
frappe://get/resource?doctype=Item&fields=["name","item_name"]
```

## Options

The following options are supported:

- `doctype`: The name of the doctype you want to interact with.
- `fields`: A list of fields you want to retrieve. For example: `["name","item_name"]`.
- `filters`: A list of filters you want to apply. For example: `{"item_name":"Test Item"}`.
- `limit_start`: The start index for the limit.
- `limit_page_length`: The number of records to retrieve.
- `order_by`: The field to order the results by.
- `order`: The order of the results. Can be `asc` or `desc`.
- `resource`: The data you want to send to the server. For example: `{"item_name":"Test Item"}`.

## Examples

### Get a list of items

```java
from("direct:getItems")
    .to("frappe://get/resource?doctype=Item&fields=[\"name\",\"item_name\"]")
    .end();
```

### Create a new item

```java
from("direct:createItem")
    .to("frappe://post/resource?doctype=Item&resource={\"item_name\":\"Test Item\"}")
    .end();
```

## Building

To build this project use

```
mvn clean install
```

For more help see the [Apache Camel documentation](https://camel.apache.org/manual/getting-started.html).
