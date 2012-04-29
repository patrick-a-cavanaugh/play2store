import java.io.File
import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2store"
    val appVersion      = "1.0.1-SNAPSHOT"

    val appDependencies = Seq(
      "org.imgscalr" % "imgscalr-lib" % "4.2",
      "postgresql" % "postgresql" % "9.1-901.jdbc4",
      "be.objectify" % "deadbolt-2_2.9.1" % "1.1.2"
      //"be.objectify" %% "deadbolt-2" % "1.1.3-SNAPSHOT"
      //"deadbolt-2" %% "deadbolt-2" % "1.1.3-SNAPSHOT",
      //"crionics" %% "play2-authenticitytoken" % "1.0-SNAPSHOT"
    )

    // Only compile the bootstrap files and any other *.less file in the stylesheets directory
    def customLessEntryPoints(base: File): PathFinder = (
      (base / "app" / "assets" / "stylesheets" / "lib" / "bootstrap" * "bootstrap.less") +++
      (base / "app" / "assets" / "stylesheets" / "lib" / "bootstrap" * "responsive.less") +++
      (base / "app" / "assets" / "stylesheets" * "*.less") )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
      //resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns),
      lessEntryPoints <<= baseDirectory(customLessEntryPoints)
      // Resolver.file("Local Repository", file("/Users/patrickc/src/Play20/repository/local"))(Resolver.ivyStylePatterns)
    )

}
