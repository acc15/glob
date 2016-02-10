# Glob pattern implementation

This is a tiny dependency-free implementation of glob patterns.

![Build status](https://travis-ci.org/acc15/glob.svg?branch=master)

License: MIT

## Maven

Latest artifacts was deployed to Maven Central:

    <dependency>
        <groupId>com.github.acc15</groupId>
        <artifactId>glob</artifactId>
        <version>1.0.0</version>
    </dependency>

## Syntax

* `?` - matches any single charater
* `*` - matches any zero-or-more characters
* `{variant1,variant2,...,variantN}` - matches any of specified variant 
    (variant can contain other expression, including nested variants)
* `**` - matches any zero-or-more path segments
* `/` - path separator (both on windows and unix). 

Note that `\` character wont work on Windows as path separator and will be treated as simple character, because:
  * This will force to write similar globs
  * At least on Linux (`ext4`) file and directory names can contain `\` character

## Examples

Recursively find all PNG files in `C:\Users\CurrentUser\photos\cats`

```Java
Glob glob = Glob.compile("photos/cats/**/*.png");
Set<Path> foundPhotos = glob.scan(Paths.get("C:","Users","CurrentUser"), TargetType.FILE);
```

Check whether specified path matches glob or not

```Java
Glob glob = Glob.compile("photos/cats/**/*.png");
boolean isPhotoOfCat = glob.test(Paths.get("photos", "cats", "trojan.exe"), TargetType.FILE);
```

`Glob` class implements `Predicate<Path>` and therefore can be used in Java 8 Stream API:

```Java
Glob glob = Glob.compile("*-cat.{jpg,png}");

List<Path> paths = Arrays.asList(
    Paths.get("the-dog.jpg"),
    Paths.get("the-cat.jpg"),
    Paths.get("the-rat.png"));

boolean isThereAnyCat = paths.stream().anyMatch(glob);
boolean isAllAreCats = paths.stream().allMatch(glob);

List<Path> foundCats = paths.stream().filter(glob).collect(Collectors.toList());
```

## Why this was written
### Java out-of-the-box globs
    
Out-of-box Java globs (
    [FileSystem#getPathMatcher(String)](https://docs.oracle.com/javase/8/docs/api/java/nio/file/FileSystem.html#getPathMatcher-java.lang.String-), 
    [Files#newDirectoryStream(Path, String)](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#newDirectoryStream-java.nio.file.Path-java.lang.String-)) 
doesn't support _normal_ zero-or-more directory match syntax (ant-like `**`).
In Java glob patterns `**` means 'any character crossing directory boundaries'.

But what if u want to find files starting with letter 'a' and having extension '.png'?

For example consider following file structure:

    /photos
        /cats
            /flying
                abc.png
                bac.png
                xyz.png
            abc.png
            bac.png
            xyz.png
    
And following glob: `photos/cats/**/a*.png`

It will match PNGs only in child directories of `photos/cats` (`photos/cats/flying`, but not in `photos/cats`) - only 1 file:

* `photos/cats/flying/abc.png`

More "correct" approach can be: `photos/cats/**a*.png`
    
This is better. Will find all needed files. 
Plus all files having 'a' letter in middle of filename.
    
Directory stream would return 4 files (instead of expected 2):

* `photos/cats/abc.png` 
* `photos/cats/bac.png`
* `photos/cats/flying/abc.png`
* `photos/cats/flying/bac.png`

Because pattern matches. 

This is just _not possible_ with this implementation.
    
    