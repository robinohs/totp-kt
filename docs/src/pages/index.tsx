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
        <ul className="hero__subtitle">
          <li>Add <b>2-factor authentication</b> to your <b>backend</b></li>
          <li>Pre-configured for <b>Google Authenticator</b>.</li>
          <li>Build your own <b>Authenticator app</b>.</li>
        </ul>
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
