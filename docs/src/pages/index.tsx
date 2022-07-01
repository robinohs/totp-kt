import Link from "@docusaurus/Link";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";
import HomepageFeatures from "@site/src/components/HomepageFeatures";
import Layout from "@theme/Layout";
import clsx from "clsx";
import React from "react";

import styles from "./index.module.css";

function HomepageHeader() {
  const { siteConfig } = useDocusaurusContext();
  return (
    <header className={clsx("hero hero--primary", styles.heroBanner)}>
      <div className="container">
        <h1 className="hero__title">{siteConfig.title}</h1>
        <p className="hero__subtitle">{siteConfig.tagline}</p>
        <p className="hero__subtitle">
          Validate <b>Authenticator codes</b> in your <b>backend</b>.
        </p>
        <div className={styles.buttons}>
          <Link
            className="button button--secondary button--lg"
            to="/docs/intro"
          >
            Get started
          </Link>
          <Link
            className="button button--secondary button--lg"
            to="https://github.com/robinohs/totp-kt"
          >
            GitHub
          </Link>
          <iframe
            className={styles.indexCtasGitHubButton}
            src="https://ghbtns.com/github-btn.html?user=robinohs&amp;repo=totp-kt&amp;type=star&amp;count=true&amp;size=large"
            width={160}
            height={30}
            title="GitHub Stars"
          />
        </div>
      </div>
    </header>
  );
}

export default function Home(): JSX.Element {
  const { siteConfig } = useDocusaurusContext();
  return (
    <Layout
      title={"Home"}
      description="Start page of the documentation for totp-kt library."
    >
      <HomepageHeader />
      <main>
        <HomepageFeatures />
      </main>
    </Layout>
  );
}
