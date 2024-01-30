# Usage

First, you need to choose a [release](https://github.com/efekos/PortfolioGenerator/releases). Every release has its own
`template.zip` file. You can install that template for a quick start.

## Setting up the template

Template contains three main files:

* `data` - Main data folder that the program will read.
* `lib` - Libraries needed to run the program.
* `PortfolioGenerator-{version}-SNAPSHOT` - The program.

`data` folder is the thing that you will mess with. This folder contains all the data program will need from you. Open
the `data` folder.

Every json file will have a `$schema` key, or a link that leads to their schema file, so you can add IntelliSense if you
use any IDE/Text Editor that was designed for coding.

> I have no idea about adding a $schema key to array objects, you can use your own ways if you know one.

Files ending with `.txt` are probably notes that you should read. They are usually a placeholder for another file that
is required.

Markdown files are either self-explanatory because of their name, or contain what they are in themselves.

Note that every file included in `template.zip` files are required. You'll get a `FileNotFoundException` if there is a
missing file.

## Running the program

When you run the program, there are three different scenarios that can happen.

* `[SUCCESS]` - Program runs every process successfully, and generates the website.
* `[ERROR]` - Program fails a process with a `JsonParseException` or a `FileNotFoundException`, which are two exceptions
that are mostly thrown because of wrong syntax or missing files.
* `[PROCESS FAIL]` - One of the processes fail with an unexpected exception.

You can run the program using `java -jar` command. Note that you must have a JRE installed to do that.

When you run the program, the program will generate the website under a `bin` folder.