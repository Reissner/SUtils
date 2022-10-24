<!-- markdownlint-disable no-trailing-spaces -->
<!-- markdownlint-disable no-inline-html -->
# Utilities used by simuline (www.simuline.eu)

This is a collection of utility classes used by simuline.
Unlike other projects, this one is intended to be shrunk in the long run.

Feel free to use anything you need in any version. 

The only class we feel shall be highlighted 
is [`Benchmarker`](./src/main/java/eu/simuline/util/Benchmarker.java). 
Essentially, it provides commands `mtic()` and `mtoc()` used to check time and memory consumption. 
Although inspired by [MATLAB `tic/toc`](https://www.mathworks.com/help/matlab/ref/tic.html), 
there are significant differences: 

- It allows not only time elapsed between `mtic()` and `mtoc()` 
  but also memory consumption (this is what the `m` stands for). 
  This may well be negative as it is just memory difference between `mtic()` and `mtoc()`. 
- The return value of `mtoc()` is a `Snapshot` object containing time and memory, whereas `mtic()` returns a number code. 
- It is hierarchical interpreting `mtic()` and `mtoc()` like opening and closing brackets which must occur in a balanced way. 
  Time and memory consumption is measured between a `mtic()` and its *according* `mtoc()` 
  allowing nested measurements. 
  In fact, `mtic()` returns the hash code of the `Snapshot` returned by the according `mtoc()`. 
  The hash code does not depend on time or memory consumption but only on the identity of the `Snapshot`. 
  That way, by return value it can be checked whether the invocations of `mtic()` and of `mtoc()` correspond. 

In fact, the properties are related: 
Memory check includes garbage collection and is slow. 
In other words, memory measurement invalidates naive time measurement. 
This problem is solved by pausing all time/memory-measurements but the innermost one 
and by doing the innermost memory measurement outside innermost time measurement. 

Besides simple `mtic()` and `mtoc()`, the `Benchmarker` also offers `pause()` and `resume()` 
pausing and resuming both time and memory measurement. 
