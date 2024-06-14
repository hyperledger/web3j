Maintainers
===========
<!-- Please keep all lists sorted alphabetically by github -->

**Active Maintainers**

<!-- web3j-maintainers group has maintainer access to web3j repo -->

| Name           | Github                 | LFID      |
|----------------|------------------------|-----------|
| Andrii Kliui   | [andrii-kl][andrii-kl] | andrii.k  |
| Conor Svensson | [conor10][conor10]     | conor     |
| George Ţebrean | [gtebrean][gtebrean]   | gtebrean  |
| Nischal Sharma | [NickSneo][NickSneo]   | nicks1106 |

[conor10]: https://github.com/conor10
[gtebrean]: https://github.com/gtebrean
[NickSneo]: https://github.com/NickSneo
[andrii-kl]: https://github.com/andrii-kl



**Emeritus Maintainers**

| Name            | Github              | LFID    |
|-----------------|---------------------|---------|
| Christian Felde | [cfelde][cfelde]    |  cfelde |

[cfelde]: https://github.com/cfelde

## Becoming a Maintainer

Web3J welcomes community contribution.
Each community member may progress to become a maintainer.

How to become a maintainer:

- Contribute significantly to the code in web3j repositories.

### Maintainers contribution requirement

The requirement to be able to be proposed as a maintainer is:

- 5 significant changes on code have been authored in this repos by the proposed maintainer and accepted (merged PRs).

### Maintainers approval process

The following steps must occur for a contributor to be "upgraded" as a maintainer:

- The proposed maintainer has the sponsorship of at least one other maintainer.
    - This sponsoring maintainer will create a proposal PR modifying the list of
      maintainers. (see [proposal PR template](#proposal-pr-template).)
    - The proposed maintainer accepts the nomination and expresses a willingness
      to be a long-term (more than 6 month) committer by adding a comment in the proposal PR.
    - The PR will be communicated in all appropriate communication channels
      including at least [web3j-contributors channel on Hyperledger Discord](https://discord.gg/hyperledger),
      the [mailing list](https://lists.hyperledger.org/g/web3j)
      and any maintainer/community call.
- Approval by at least 2 current maintainers within two weeks of the proposal or
  an absolute majority (half the total + 1) of current maintainers.
    - Maintainers will vote by approving the proposal PR.
- No veto raised by another maintainer within the voting timeframe.
    - All vetoes must be accompanied by a public explanation as a comment in the
      proposal PR.
    - A veto can be retracted, in that case the voting timeframe is reset and all approvals are removed.
    - It is bad form to veto, retract, and veto again.

The proposed maintainer becomes a maintainer either:

- when two weeks have passed without veto since the third approval of the proposal PR,
- or an absolute majority of maintainers approved the proposal PR.

In either case, no maintainer raised and stood by a veto.

## Removing Maintainers

Being a maintainer is not a status symbol or a title to be maintained indefinitely.

It will occasionally be necessary and appropriate to move a maintainer to emeritus status.

This can occur in the following situations:

- Resignation of a maintainer.
- Violation of the Code of Conduct warranting removal.
- Inactivity.
    - A general measure of inactivity will be no commits or code review comments
      for two reporting quarters, although this will not be strictly enforced if
      the maintainer expresses a reasonable intent to continue contributing.
    - Reasonable exceptions to inactivity will be granted for known long term
      leave such as parental leave and medical leave.
- Other unspecified circumstances.

As for adding a maintainer, the record and governance process for moving a
maintainer to emeritus status is recorded using review approval in the PR making that change.

Returning to active status from emeritus status uses the same steps as adding a
new maintainer.

Note that the emeritus maintainer always already has the required significant contributions.
There is no contribution prescription delay.

## Proposal PR template

```markdown
I propose to add [maintainer github handle] as a Web3J project maintainer.

[maintainer github handle] contributed with many high quality commits:

- [list significant achievements]

Here are [their past contributions on Web3J project](https://github.com/hyperledger/web3j/commits?author=[user github handle]).

Voting ends two weeks from today.

For more information on this process see the Becoming a Maintainer section in the MAINTAINERS.md file.
```