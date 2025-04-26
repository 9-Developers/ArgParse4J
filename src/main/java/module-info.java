module tech.ixirsii.argparse4j {
    requires static lombok;
    requires org.slf4j;

    exports tech.ixirsii.parse.command;
    exports tech.ixirsii.parse.event;
    exports tech.ixirsii.parse.exception;
    exports tech.ixirsii.parse.parser;
}
