# Zipkin Forwarder

> alpha stage

Zipkin Storage implementation to forward Spans to another collector.

```
... client ...     ............. forwarder ...............   ..transport..   .............. zipkin server .......... 
[ url sender ] --> [ [ url collector ]->[ kafka sender ] ] --> ( kafka ) --> [ [ kafka collector ]->[ es storage ] ]
```
