# Democracy voucher test implementation
## introduction

The civic project that this code implements goes by different names:
"Democracy Vouchers" (as implemented by [Seattle][Seattle_Democracy_Vouchers]) or
"Democracy Dollars" (as implemented by [Oakland][Oakland_Democracy_Dollars]).
You can read more about it on [Wikipedia][Wikipedia_Democracy_voucher].

The purpose of this project is to implement a testbed for ideas for the City of Oakland project,
with the hope of making something useful for future governments' democracy voucher programs.
A primary goal of Oakland's project is to introduce digital vouchers,
vis-à-vis Seattle's program, which focused on physical vouchers.
Digital vouchers have the promise of making the system cheaper to implement and manage
but also introduce new security considerations.

## running the application

This software runs as a Docker application.<sup>*</sup>
It requires installing the `docker` command line application.

The project includes a GitHub Actions workflow that publishes the **static site content** to GitHub Pages.
These pages may not be fully functional without the back-end services, of course,
but it provides a quick way to share that part of the system.

<sup>*</sup>&nbsp;This isn't entirely true yet.
The back end can run in a Docker container, but we're not using it in the project yet.
And the front end isn't configured for Docker deployment yet.

## developing the application

See [doc/Developing.md](doc/Developing.md).

[Oakland_Democracy_Dollars]: https://www.oaklandca.gov/topics/democracy-dollars
[Seattle_Democracy_Vouchers]: https://www.seattle.gov/democracyvoucher
[Wikipedia_Democracy_voucher]: https://en.wikipedia.org/wiki/Democracy_voucher
