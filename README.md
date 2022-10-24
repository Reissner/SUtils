<!-- markdownlint-disable no-trailing-spaces -->
<!-- markdownlint-disable no-inline-html -->
# Utilities used by simuline (www.simuline.eu)

This is a collection of utility classes used by simuline.
Unlike other projects, this one is intended to be shrunk in the long run.

Feel free to use anything you need in any version. 

The only class we feel shall be highlighted 
is [Benchmarker](./src/main/java/eu/simuline/util/Benchmarker.java). 
Essentially, it provides commands `tic()` and `toc()` used to check time and memory consumption. 
Although inspired by [MATLAB](https://www.mathworks.com/help/matlab/ref/tic.html), 
there are significant differences: 

- it allows not only time elapsed between `tic()` and `toc()` 
  but also memory consumption, which may well be negative as it is just memory difference between tic and toc. 
- it is hierarchical interpreting tic and toc like opening and closing brackets which must occur in a balanced way. 
  Time and memory consumption is measured between a tic and its *according* toc 
  allowing nested measurements. 
