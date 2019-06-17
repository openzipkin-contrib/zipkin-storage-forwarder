# Zipkin Storage: Reporter

> alpha stage

Zipkin Storage implementation to forward Spans to another collector.

```
[ url sender ] --> [ [ url collector ]->[ kafka sender ] ] --> ( kafka ) --> [ [ kafka collector ]->[ es storage ] ]
```
