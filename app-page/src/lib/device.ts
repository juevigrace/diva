export function getDeviceLabel(userAgent: string): string {
  const ua = userAgent.toLowerCase();

  const browser: string = (() => {
    if (ua.includes('edg/') || ua.includes('edge/')) {
      const m = userAgent.match(/Edg\/(\d+)/i);
      return m ? `Edge ${m[1]}` : 'Edge';
    }
    if (ua.includes('opr/') || ua.includes('opera/')) {
      const m = userAgent.match(/(?:OPR|Opera)\/(\d+)/i);
      return m ? `Opera ${m[1]}` : 'Opera';
    }
    if (ua.includes('chrome/') && !ua.includes('chromium/') && !ua.includes('edg/')) {
      const m = userAgent.match(/Chrome\/(\d+)/i);
      return m ? `Chrome ${m[1]}` : 'Chrome';
    }
    if (ua.includes('firefox/')) {
      const m = userAgent.match(/Firefox\/(\d+)/i);
      return m ? `Firefox ${m[1]}` : 'Firefox';
    }
    if (ua.includes('safari/') && !ua.includes('chrome/')) {
      const m = userAgent.match(/Version\/(\d+)/i);
      return m ? `Safari ${m[1]}` : 'Safari';
    }
    return 'web';
  })();

  const os: string = (() => {
    if (ua.includes('windows nt')) return 'Windows';
    if (ua.includes('mac os x')) return 'macOS';
    if (ua.includes('linux') && !ua.includes('android')) return 'Linux';
    if (ua.includes('android')) return 'Android';
    if (ua.includes('iphone') || ua.includes('ipad')) return 'iOS';
    if (ua.includes('cros')) return 'ChromeOS';
    return 'Unknown';
  })();

  return `web - ${browser} - ${os}`;
}
