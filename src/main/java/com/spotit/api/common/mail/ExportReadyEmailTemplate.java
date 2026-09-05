package com.spotit.api.common.mail;

public final class ExportReadyEmailTemplate {
    private static final String ACCENT = "#C04E68";
    private static final String ACCENT_SOFT = "#DC5A74";
    private static final String INK = "#2E2429";
    private static final String MUTED = "#8A7377";
    private static final String PANEL_BG = "#FFF6F5";
    private static final String PAGE_BG = "#F4EEEE";

    private ExportReadyEmailTemplate() {
    }

    public static String html(String greeting, int logCount, int pointsEntryCount) {
        return """
                <!doctype html>
                <html>
                  <body style="margin:0;padding:0;background:%s;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;">
                    <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background:%s;padding:32px 16px;">
                      <tr>
                        <td align="center">
                          <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="max-width:440px;background:#FFFFFF;border-radius:20px;overflow:hidden;border:1px solid #F0E3E3;">
                            <tr>
                              <td style="padding:28px 32px 0 32px;text-align:center;">
                                <div style="font-size:20px;font-weight:700;color:%s;letter-spacing:-0.3px;">Spot<span style="color:%s;"> it</span></div>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:20px 36px 8px 36px;text-align:center;">
                                <h1 style="margin:0;font-size:21px;line-height:27px;font-weight:600;color:%s;">Your data export is ready</h1>
                                <p style="margin:10px 0 0 0;font-size:13px;line-height:19px;color:%s;">Hi %s, we've attached a full copy of your Spot it data as a spreadsheet (CSV) — it opens directly in Excel, Numbers or Google Sheets.</p>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:22px 36px 4px 36px;">
                                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background:%s;border-radius:14px;">
                                  <tr>
                                    <td style="padding:16px 18px;">
                                      <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                                        <tr>
                                          <td style="font-size:12px;color:%s;padding:4px 0;">Profile</td>
                                          <td align="right" style="font-size:12px;color:%s;font-weight:600;padding:4px 0;">Included</td>
                                        </tr>
                                        <tr>
                                          <td style="font-size:12px;color:%s;padding:4px 0;">Cycle logs</td>
                                          <td align="right" style="font-size:12px;color:%s;font-weight:600;padding:4px 0;">%d entries</td>
                                        </tr>
                                        <tr>
                                          <td style="font-size:12px;color:%s;padding:4px 0;">Points history</td>
                                          <td align="right" style="font-size:12px;color:%s;font-weight:600;padding:4px 0;">%d entries</td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:24px 36px 28px 36px;text-align:center;">
                                <p style="margin:0;font-size:11px;line-height:17px;color:%s;">Didn't request this? Someone with access to your account may have requested an export — check your account security if this wasn't you.</p>
                              </td>
                            </tr>
                          </table>
                          <p style="margin:20px 0 0 0;font-size:11px;color:%s;">&copy; Spot it &middot; This is an automated message, please don't reply.</p>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """.formatted(
                PAGE_BG, PAGE_BG,
                INK, ACCENT_SOFT,
                INK,
                MUTED, greeting,
                PANEL_BG,
                MUTED, INK,
                MUTED, INK, logCount,
                MUTED, INK, pointsEntryCount,
                MUTED,
                MUTED
        );
    }

    public static String text(String greeting, int logCount, int pointsEntryCount) {
        return """
                Your data export is ready

                Hi %s, we've attached a full copy of your Spot it data as a spreadsheet (CSV) — it opens directly in Excel, Numbers or Google Sheets.

                Profile: included
                Cycle logs: %d entries
                Points history: %d entries

                Didn't request this? Check your account security if this wasn't you.

                — Spot it
                """.formatted(greeting, logCount, pointsEntryCount);
    }
}
