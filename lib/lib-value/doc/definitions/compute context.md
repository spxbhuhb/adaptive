# Compute context

A compute context is used to provide context for computations in [value workers](def://).

Data modification functions of [value workers](def://) are usually not visible for general
code to enforce data integrity. The compute context provides temporary access to these
modification functions while a code that modifies data executes.