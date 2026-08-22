package com.spotit.api.common.mail;

/**
 * Builds the HTML + plain-text bodies for OTP emails (signup verification and password
 * reset). Kept as static string templates rather than a template engine — the two OTP
 * emails are the only transactional mail this app sends, so pulling in Thymeleaf/Freemarker
 * for it isn't worth the dependency.
 */
public final class OtpEmailTemplate {

    private static final String ACCENT = "#C04E68";
    private static final String ACCENT_SOFT = "#DC5A74";
    private static final String INK = "#2E2429";
    private static final String MUTED = "#8A7377";
    private static final String PANEL_BG = "#FFF6F5";
    private static final String PAGE_BG = "#F4EEEE";
    private static final String CODE_TILE_BG = "#FCE0DE";

    private OtpEmailTemplate() {
    }

    public static String html(String greeting, String code, String heading, String introLine, long ttlMinutes) {
        String spacedCode = String.join(" ", code.split(""));
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
                                <h1 style="margin:0;font-size:21px;line-height:27px;font-weight:600;color:%s;">%s</h1>
                                <p style="margin:10px 0 0 0;font-size:13px;line-height:19px;color:%s;">Hi %s, %s</p>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:22px 36px 4px 36px;">
                                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                                  <tr>
                                    <td align="center" style="background:%s;border-radius:14px;padding:18px 12px;">
                                      <span style="font-size:24px;font-weight:700;letter-spacing:8px;color:%s;font-family:'SF Mono',Consolas,Menlo,monospace;">%s</span>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:14px 36px 0 36px;text-align:center;">
                                <p style="margin:0;font-size:12px;line-height:18px;color:%s;">This code expires in <strong style="color:%s;">%d minutes</strong>.</p>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:24px 36px 28px 36px;text-align:center;">
                                <p style="margin:0;font-size:11px;line-height:17px;color:%s;">If you didn't request this, you can safely ignore this email &mdash; no changes will be made to your account.</p>
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
                INK, heading,
                MUTED, greeting, introLine,
                CODE_TILE_BG, ACCENT, spacedCode,
                MUTED, ACCENT,
                ttlMinutes,
                MUTED,
                MUTED
        );
    }

    public static String text(String greeting, String code, String heading, String introLine, long ttlMinutes) {
        return """
                %s

                Hi %s, %s

                Your code: %s

                This code expires in %d minutes. If you didn't request this, you can safely ignore this email — no changes will be made to your account.

                — Spot it
                """.formatted(heading, greeting, introLine, code, ttlMinutes);
    }
}
