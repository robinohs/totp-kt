import clsx from "clsx";
import React from "react";
import styles from "./styles.module.css";

type FeatureItem = {
  title: string;
  Svg: React.ComponentType<React.ComponentProps<"svg">>;
  description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
  {
    title: "Easy to Use",
    Svg: require("@site/static/img/features_easytouse.svg").default,
    description: (
      <>
        No complicated understanding of Google Authenticator specification is
        required, as the library's Totp generator is pre-configured.
      </>
    ),
  },
  {
    title: "Security Driven",
    Svg: require("@site/static/img/features_security.svg").default,
    description: (
      <>
        Security was the main focus during development. A code coverage of
        almost 100% as well as scans with sonarcloud for
        vulnerabilities support that fact.
      </>
    ),
  },
  {
    title: "Powered by Kotlin",
    Svg: require("@site/static/img/features_kotlin.svg").default,
    description: (
      <>
        Native Kotlin Library using best practices and features of the language.
        Free from boilerplate code and other Java drawbacks.
      </>
    ),
  },
];

function Feature({ title, Svg, description }: FeatureItem) {
  return (
    <div className={clsx("col col--4")}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <h3>{title}</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): JSX.Element {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
