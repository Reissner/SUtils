/**
 * Utility classes: very inhomogeneous. 
 * These are used in projects Arith, Relana, ArithIntOctave and Testhelpers. 
 * In the long run, some classes will be eliminated, 
 * maybe replaced by classes more commonly used. 
 * To decide this, it must be analyzed which projects need which classes. 
 * 
 * | Class                      | Arith    | Relana | ArithIntOct.. | Testhelp..  |
 * | -------------------------- | -------- | ------ | ------------- | ----------- |
 * | {@link AbstractMultiSet}   | -        |  -     | -             |  -          |
 * | {@link ArraysExt}          | ContFrac |        |               |             |
 * | {@link BasicTypesCompatibilityChecker}- | -    |   -           |  -          | internal
 * | {@link Benchmarker}        | perf.    | -      |
 * | {@link BitSetList}         | buffer   |        |               |             |
 * | {@link Caster}             |   -      | -      | -             |  -          |internal
 * | {@link CollectionsExt}     | multip   | -      | -             |  -          |
 * | {@link Comparators}        | l2ra     | -      | -             |  -          |
 * | {@link CyclicArrayList}    | graphDv  | -      | -             |  -          |
 * | {@link CyclicIterator}     | graphDv  | -      | -             |  -          |
 * | {@link CyclicList}         | graphDv  | -      | -             |  -          |
 * | {@link DataModel}          | fpga     | -      | -             |  -          |
 * | {@link DetOs}              | fpga     |        |               |             |
 * | {@link EmptyCyclicListException}    -      | -             |  -          |internal
 * | {@link FIFOList}           | -        | -      | -             |  -          |internal
 * | {@link Finder}             | tex      | -      | parser        |  -          |
 * | {@link HashMultiSet}       | -        |  -     | -             |  -          |
 * | {@link JavaPath}           | -        |  -     | -             |  x          |
 * | {@link ListMap}            |fpga      |  -     | -             |  -          | sgml
 * | {@link ListSet}            | -        |  -     | -             |  -          | internal
 * | {@link MultiSet}           | x        | -      |   -           |  -          |
 * | {@link MultiSetIterator}   | x        | -      |   -           |  -          |
 * | {@link NotYetImplementedException} |- |               |             |
 * |         PathFinder         | -        | -     |  -            |  -          |
 * | {@link RealRepresentation} | -        | -     |  -            |  -          |
 * | {@link SoftEnum}           | -        | -     |  -            |  -          |
 * | {@link SortedMultiSet}     | x        | -     |   -           |  -          |
 * | {@link StringPool}         | -        | -     |  -            |  -          |
 * | {@link Strings}            | tex      | -     |  -            |  x          |
 * | {@link SwingWorker}        | -        | -     |  -            |  -          |
 * | {@link TreeMultiSet}       | x        | -     |   -           |  -          |
 * | {@link TwoSidedList}       | x        |       |               |             |
 * | {@link VMArgs}             | -        | -     | -             | -           |
 * <ul>
 * <li>
 * Framework providing the {@link MultiSet}s, 
 * plain and sorted {@link SortedMultiSet}, both interfaces 
 * and implementing classes: {@link AbstractMultiSet} 
 * which is the base class of all concrete classes. 
 * There are two, {@link HashMultiSet} which is just a {@link MultiSet} 
 * and {@link TreeMultiSet} which is even a {@link SortedMultiSet}. 
 * **** bad design: immutable. of TreeMultiSet set and of HashMultiSet
 * <li>
 * Cyclic lists: {@link CyclicList}, {@link CyclicArrayList} 
 * and {@link CyclicIterator}, and also {@link EmptyCyclicListException}
 * <li>
 * Two sided lists: {@link TwoSidedList}
 * <li>
 * Extension classes {@link CollectionsExt} and {@link ArraysExt} 
 * <li>
 * very inhomogeneous rest. 
 * </ul>
 *
 * <code>x</code> ***** NOT YET COMPLETE. 
 */
package eu.simuline.util;
